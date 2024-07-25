package dev.mjanusz.recruitmentapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryEntity
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryFavView
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<RepositoryEntity>)

    @Query("SELECT * FROM repository_fav_view")
    fun getRepositoriesWithFav(): Flow<List<RepositoryFavView>>

    @Query("DELETE FROM repository")
    fun deleteAll()
}