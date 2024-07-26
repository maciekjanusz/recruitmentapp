package dev.mjanusz.recruitmentapp.data

import dev.mjanusz.recruitmentapp.data.local.dao.RepositoryDetailsDao
import dev.mjanusz.recruitmentapp.data.local.model.toEntity
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepositorySource @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val repositoryDetailsDao: RepositoryDetailsDao
) {

    suspend fun fetchAndUpdateRepositoryDetails(owner: String, repo: String) = withContext(Dispatchers.IO) {
        val repoDetails = gitHubApi.getRepositoryDetails(owner, repo)
        repositoryDetailsDao.upsertRepositoryDetails(repoDetails.toEntity())
    }

    fun getRepositoryDetails(owner: String, repo: String) =
        repositoryDetailsDao.getRepositoryDetails("$owner/$repo")
}