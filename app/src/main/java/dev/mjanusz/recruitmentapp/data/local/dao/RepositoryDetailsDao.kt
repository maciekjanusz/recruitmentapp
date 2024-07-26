package dev.mjanusz.recruitmentapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDetailsDao {

    @Upsert
    suspend fun upsertRepositoryDetails(repositoryDetailsEntity: RepositoryDetailsEntity)

    @Query("SELECT * FROM repository_details WHERE fullName = :fullName")
    fun getRepositoryDetails(fullName: String): Flow<RepositoryDetailsEntity>
}