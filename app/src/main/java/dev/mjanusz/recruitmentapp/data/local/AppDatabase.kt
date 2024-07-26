package dev.mjanusz.recruitmentapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.mjanusz.recruitmentapp.data.local.dao.FavouritesDao
import dev.mjanusz.recruitmentapp.data.local.dao.LanguageDao
import dev.mjanusz.recruitmentapp.data.local.dao.RepositoriesDao
import dev.mjanusz.recruitmentapp.data.local.dao.RepositoryDetailsDao
import dev.mjanusz.recruitmentapp.data.local.dao.UserDao
import dev.mjanusz.recruitmentapp.data.local.dao.UserDetailsDao
import dev.mjanusz.recruitmentapp.data.local.dao.UserSearchResultsDao
import dev.mjanusz.recruitmentapp.data.local.model.FavouriteRepoEntity
import dev.mjanusz.recruitmentapp.data.local.model.LanguageEntity
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryDetailsEntity
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryEntity
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryFavView
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryOwnerEntity
import dev.mjanusz.recruitmentapp.data.local.model.UserDetailsEntity
import dev.mjanusz.recruitmentapp.data.local.model.UserEntity
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity

const val DATABASE_DEFAULT_NAME = "app_database"

@Database(
    entities = [
        LanguageEntity::class,
        RepositoryEntity::class,
        FavouriteRepoEntity::class,
        RepositoryDetailsEntity::class,
        UserEntity::class,
        UserSearchResultEntity::class,
        UserDetailsEntity::class
    ],
    views = [
        RepositoryFavView::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun languageDao(): LanguageDao

    abstract fun repositoryDao(): RepositoriesDao

    abstract fun favouritesDao(): FavouritesDao

    abstract fun repositoryDetailsDao(): RepositoryDetailsDao

    abstract fun userDao(): UserDao

    abstract fun userDetailsDao(): UserDetailsDao

    abstract fun userSearchResultsDao(): UserSearchResultsDao
}