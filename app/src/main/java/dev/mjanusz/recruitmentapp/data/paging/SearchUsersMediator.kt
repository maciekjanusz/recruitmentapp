package dev.mjanusz.recruitmentapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import dev.mjanusz.recruitmentapp.data.local.dao.UserSearchResultsDao
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity
import dev.mjanusz.recruitmentapp.data.local.model.toUserSearchResultEntity
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

@OptIn(ExperimentalPagingApi::class)
class SearchUsersMediator(
    val remoteSource: GitHubApi,
    val dao: UserSearchResultsDao,
    val appDatabase: AppDatabase,
    val query: String,
) : RemoteMediator<Int, UserSearchResultEntity>() {

    // used for simplicity as it works in this specific case,
    // preferably implemented with keys cached in db
    private var nextPage = AtomicInteger(0)

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserSearchResultEntity>
    ): MediatorResult {
        try {
            if (loadType == LoadType.PREPEND) return MediatorResult.Success(true)
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                    nextPage.set(0)
                } else if (loadType == LoadType.APPEND) {
                    nextPage.getAndIncrement()
                }
                val nextRemotePage = nextPage.get() + 1
                val result = remoteSource.searchUsers(query = query, page = nextRemotePage)
                dao.upsertList(result.items.map { it.toUserSearchResultEntity() })
            }
            return MediatorResult.Success(false)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return if (e.code() == 422)
                MediatorResult.Success(true)
            else
                MediatorResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_KEY = 0
    }
}