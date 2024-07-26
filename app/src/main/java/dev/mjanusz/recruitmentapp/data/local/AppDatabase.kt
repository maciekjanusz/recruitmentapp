package dev.mjanusz.recruitmentapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.mjanusz.recruitmentapp.data.local.dao.FavouritesDao
import dev.mjanusz.recruitmentapp.data.local.dao.LanguageDao
import dev.mjanusz.recruitmentapp.data.local.dao.RepositoriesDao
import dev.mjanusz.recruitmentapp.data.local.dao.RepositoryDetailsDao
import dev.mjanusz.recruitmentapp.data.local.model.FavouriteRepoEntity
import dev.mjanusz.recruitmentapp.data.local.model.LanguageEntity
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryDetailsEntity
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryEntity
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryFavView

const val DATABASE_DEFAULT_NAME = "app_database"

@Database(
    entities = [
        LanguageEntity::class,
        RepositoryEntity::class,
        FavouriteRepoEntity::class,
        RepositoryDetailsEntity::class,
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

}