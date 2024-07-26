package dev.mjanusz.recruitmentapp.data.remote

import dev.mjanusz.recruitmentapp.data.remote.model.LanguagesDto
import dev.mjanusz.recruitmentapp.data.remote.model.TrendingReposDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubTrendingApi {

    @GET("/trending")
    suspend fun getLanguages(): LanguagesDto

    @GET("/trending/{languagePath}")
    suspend fun getTrendingRepos(
        @Path("languagePath") language: String = "",
        @Query("since") range: TrendingDateRange = TrendingDateRange.DAILY
    ): TrendingReposDto
}

enum class TrendingDateRange(private val range: String) {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly");

    override fun toString() = range

    fun displayString() =
        range.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}