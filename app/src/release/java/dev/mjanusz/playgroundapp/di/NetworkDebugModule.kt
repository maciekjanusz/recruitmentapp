package dev.mjanusz.recruitmentapp.di

import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
class NetworkDebugModule {

    @Provides
    fun provideEmptyInterceptorSet() = emptySet<Interceptor>()
}