package dev.mjanusz.playgroundapp.di

import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
class NetworkDebugModule {

    @Provides
    fun provideEmptyInterceptorSet() = emptySet<Interceptor>()
}