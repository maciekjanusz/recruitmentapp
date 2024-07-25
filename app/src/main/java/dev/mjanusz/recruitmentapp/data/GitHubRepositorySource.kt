package dev.mjanusz.recruitmentapp.data

import dev.mjanusz.recruitmentapp.data.local.dao.RepositoriesDao
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepositorySource @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val repositoriesDao: RepositoriesDao
) {
}