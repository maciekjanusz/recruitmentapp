package dev.mjanusz.recruitmentapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.mjanusz.recruitmentapp.data.local.dao.UserDao
import dev.mjanusz.recruitmentapp.data.local.dao.UserDetailsDao
import dev.mjanusz.recruitmentapp.data.local.dao.UserSearchResultsDao
import dev.mjanusz.recruitmentapp.data.local.model.UserDetails
import dev.mjanusz.recruitmentapp.data.local.model.UserEntity
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity

const val DATABASE_DEFAULT_NAME = "app_database"

@Database(
    entities = [
        UserEntity::class,
        UserSearchResultEntity::class,
        UserDetails::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun userDetailsDao(): UserDetailsDao

    abstract fun userSearchResultsDao(): UserSearchResultsDao
}