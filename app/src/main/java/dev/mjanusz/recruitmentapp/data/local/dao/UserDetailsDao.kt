package dev.mjanusz.recruitmentapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mjanusz.recruitmentapp.data.local.model.UserDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetails(userDetails: UserDetails)

    @Query("SELECT * FROM userDetails WHERE userDetails.login = :username")
    fun getUserDetails(username: String): Flow<UserDetails>

    @Query("DELETE FROM userDetails")
    suspend fun clearAllDetails()
}