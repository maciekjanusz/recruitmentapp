package dev.mjanusz.recruitmentapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.mjanusz.recruitmentapp.data.local.model.UserEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertList(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun getUsersList(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM users")
    suspend fun clearAll()
}