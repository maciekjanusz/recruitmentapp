package dev.mjanusz.recruitmentapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.mjanusz.recruitmentapp.BuildConfig
import dev.mjanusz.recruitmentapp.data.remote.AuthInterceptor
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(
    includes = [
        NetworkDebugModule::class
    ]
)
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideDefaultNetworkConfig() = NetworkConfig(
        BuildConfig.GITHUB_API_BASE_URL,
        BuildConfig.GITHUB_API_TOKEN
    )

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
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json, networkConfig: NetworkConfig): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(networkConfig.baseUrl)
            .build()

    @Provides
    @Singleton
    fun provideGitHubApi(retrofit: Retrofit): GitHubApi =
        retrofit.create(GitHubApi::class.java)

}