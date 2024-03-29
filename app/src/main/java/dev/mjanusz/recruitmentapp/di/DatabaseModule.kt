package dev.mjanusz.recruitmentapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import dev.mjanusz.recruitmentapp.data.local.DATABASE_DEFAULT_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, DATABASE_DEFAULT_NAME
        ).build()

    @Provides
    @Singleton
    fun provideUsersDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideUserDetailsDao(appDatabase: AppDatabase) = appDatabase.userDetailsDao()

    @Provides
    @Singleton
    fun provideUserSearchResultsDao(appDatabase: AppDatabase) = appDatabase.userSearchResultsDao()
}