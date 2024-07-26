package dev.mjanusz.recruitmentapp.data.remote

import dev.mjanusz.recruitmentapp.data.remote.model.RepositoryDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("/repos/{owner}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): RepositoryDetailsDto
}