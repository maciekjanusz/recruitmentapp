package dev.mjanusz.recruitmentapp.data

import dev.mjanusz.recruitmentapp.common.LoadingState
import dev.mjanusz.recruitmentapp.common.Success
import dev.mjanusz.recruitmentapp.data.local.dao.UserDetailsDao
import dev.mjanusz.recruitmentapp.data.local.model.UserDetailsEntity
import dev.mjanusz.recruitmentapp.data.local.model.toUserDetails
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDetailsRepository @Inject constructor(
    private val githubApi: GitHubApi,
    private val userDetailsDao: UserDetailsDao,
) {

    // todo: call periodically, or on startup - different options
    @Suppress("unused")
    suspend fun clearCache() {
        userDetailsDao.clearAllDetails()
    }

    suspend fun getUserDetails(
        username: String
    ): Flow<LoadingState<UserDetailsEntity>> {
        // single emission flow or error
        val fetchFlow =
            flow { emit(githubApi.getUserDetails(username)) }
                .onEach {
                    userDetailsDao.insertUserDetails(it.toUserDetails())
                }
                .map { LoadingState.success(it.toUserDetails()) } // for type inference
                .drop(1)
                .catch {
                    emit(LoadingState.failure(it))
                }
        // continuous flow of changes from db
        val cacheFlow = userDetailsDao.getUserDetails(username)
            .filterNotNull()
            .map { LoadingState.success(it) }

        return merge(fetchFlow, cacheFlow) // concurrent exec
            .runningFold(LoadingState.loading<UserDetailsEntity>()) {
                // if there was success already, do not emit subsequent failure
                    accumulator, value ->
                if (accumulator is Success<UserDetailsEntity> && value !is Success<UserDetailsEntity>)
                    accumulator else value
            }
            .flowOn(Dispatchers.IO)
    }
}