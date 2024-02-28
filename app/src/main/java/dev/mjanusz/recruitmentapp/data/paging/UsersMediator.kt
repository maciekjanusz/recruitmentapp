package dev.mjanusz.recruitmentapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import dev.mjanusz.recruitmentapp.data.local.dao.UserDao
import dev.mjanusz.recruitmentapp.data.local.model.UserEntity
import dev.mjanusz.recruitmentapp.data.local.model.toUserEntity
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UsersMediator(
    private val remoteSource: GitHubApi,
    private val dao: UserDao,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UserEntity>): MediatorResult {
        try {
            val sinceId = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> 0
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.id ?: 0
                }
            }
            val result = remoteSource.getUsers(sinceId)
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                }
                dao.upsertList(result.map { it.toUserEntity() })
            }
            return MediatorResult.Success(result.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_KEY = 0
    }
}