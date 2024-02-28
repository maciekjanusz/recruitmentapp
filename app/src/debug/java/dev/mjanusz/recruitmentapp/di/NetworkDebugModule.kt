package dev.mjanusz.recruitmentapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkDebugModule {

    @Provides
    @Singleton
    @IntoSet
    fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().also {
        it.setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }
}