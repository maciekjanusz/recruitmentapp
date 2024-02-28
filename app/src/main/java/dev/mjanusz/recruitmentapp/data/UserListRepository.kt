package dev.mjanusz.recruitmentapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import dev.mjanusz.recruitmentapp.data.local.dao.UserDao
import dev.mjanusz.recruitmentapp.data.local.model.UserEntity
import dev.mjanusz.recruitmentapp.data.paging.UsersMediator
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserListRepository @Inject constructor(
    private val userDao: UserDao,
    private val gitHubApi: GitHubApi,
    private val appDatabase: AppDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getUsersPager(): Flow<PagingData<UserEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = GitHubApi.PAGE_SIZE,
                prefetchDistance = GitHubApi.PAGE_SIZE / 2,
                initialLoadSize = GitHubApi.PAGE_SIZE * 2,
            ),
            initialKey = UsersMediator.INITIAL_KEY,
            remoteMediator = UsersMediator(gitHubApi, userDao, appDatabase),
            pagingSourceFactory = { userDao.getUsersList() }
        ).flow
}