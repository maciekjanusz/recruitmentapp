package dev.mjanusz.recruitmentapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import dev.mjanusz.recruitmentapp.data.local.dao.UserSearchResultsDao
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity
import dev.mjanusz.recruitmentapp.data.paging.SearchUsersMediator
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSearchResultsRepository @Inject constructor(
    private val dao: UserSearchResultsDao,
    private val gitHubApi: GitHubApi,
    private val appDatabase: AppDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getSearchResults(query: String): Flow<PagingData<UserSearchResultEntity>> {
        return Pager(
            config = DEFAULT_PAGING_CONFIG,
            initialKey = SearchUsersMediator.INITIAL_KEY,
            remoteMediator = SearchUsersMediator(
                query = query,
                remoteSource = gitHubApi,
                dao = dao,
                appDatabase = appDatabase
            ),
            pagingSourceFactory = { dao.getSearchResults() } // todo: decide
        ).flow
    }

    companion object {
        val DEFAULT_PAGING_CONFIG = PagingConfig(
            pageSize = GitHubApi.PAGE_SIZE,
            prefetchDistance = 1
        )
    }
}