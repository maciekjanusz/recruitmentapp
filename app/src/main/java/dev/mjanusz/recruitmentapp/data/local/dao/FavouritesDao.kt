package dev.mjanusz.recruitmentapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mjanusz.recruitmentapp.data.local.model.FavouriteRepoEntity

@Dao
interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(entity: FavouriteRepoEntity)

    @Query("DELETE FROM favourite_repo WHERE repoUrl = :repoUrl")
    suspend fun deleteFavourite(repoUrl: String)
}
