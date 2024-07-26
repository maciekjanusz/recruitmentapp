package dev.mjanusz.recruitmentapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.mjanusz.recruitmentapp.data.remote.AuthInterceptor
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import dev.mjanusz.recruitmentapp.data.remote.GithubTrendingApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import pl.droidsonroids.jspoon.Jspoon
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module(
    includes = [
        NetworkDebugModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideDefaultNetworkConfig() = NetworkConfig()
        /*apiBaseUrl = BuildConfig.GITHUB_API_BASE_URL,
        siteBaseUrl = BuildConfig.GITHUB_SITE_BASE_URL,
        apiToken = BuildConfig.GITHUB_API_TOKEN*/
        // this code is for build config field injected NetworkConfig

    @Provides
    @IntoSet
    fun provideAuthInterceptor(config: NetworkConfig): Interceptor =
        AuthInterceptor(config.apiToken)

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient {
        val builder = OkHttpClient.Builder()
        interceptors.forEach { builder.addNetworkInterceptor(it) }
        return builder.build()
    }

    @Provides
    fun provideJson() = Json(Json) {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @Named("scraper")
    fun provideScraperRetrofit(okHttpClient: OkHttpClient, networkConfig: NetworkConfig): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(JspoonConverterFactory.create(Jspoon.create()))
            .baseUrl(networkConfig.siteBaseUrl)
            .build()


    @Provides
    @Singleton
    @Named("api")
    fun provideApiRetrofit(okHttpClient: OkHttpClient, json: Json, networkConfig: NetworkConfig): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(networkConfig.apiBaseUrl)
            .build()

    @Provides
    @Singleton
    fun provideGitHubTrendingApi(@Named("scraper") retrofit: Retrofit): GithubTrendingApi =
        retrofit.create(GithubTrendingApi::class.java)

    @Provides
    @Singleton
    fun provideGitHubApi(@Named("api") retrofit: Retrofit): GitHubApi =
        retrofit.create(GitHubApi::class.java)

}