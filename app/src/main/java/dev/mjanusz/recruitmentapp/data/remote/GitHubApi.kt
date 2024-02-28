package dev.mjanusz.recruitmentapp.data.remote

import dev.mjanusz.recruitmentapp.data.remote.model.GitHubUserDetailsDto
import dev.mjanusz.recruitmentapp.data.remote.model.GitHubUserDto
import dev.mjanusz.recruitmentapp.data.remote.model.UserSearchDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    companion object {
        const val PAGE_SIZE = 30
    }

    /*
<https://api.github.com/users?since=46>; rel="next",
<https://api.github.com/users{?since}>; rel="first"
<https://api.github.com/users?since=91>; rel="next",
<https://api.github.com/users{?since}>; rel="first"
*/
    @GET("/users")
    suspend fun getUsers(
        @Query("since") since: Long,
        @Query("per_page") pageSize: Int = PAGE_SIZE
    ): List<GitHubUserDto>

    @GET("/users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String
    ): GitHubUserDetailsDto

    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") pageSize: Int = PAGE_SIZE,
        @Query("sort") sortBy: String? = null, // Can be one of: followers, repositories, joined
        @Query("order") sortOrder: String = "desc",
        @Query("page") page: Int
    ): UserSearchDto
}