package dev.mjanusz.recruitmentapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mjanusz.recruitmentapp.data.local.model.LanguageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguages(languages: List<LanguageEntity>)

    @Query("SELECT * FROM language")
    fun getLanguages(): Flow<List<LanguageEntity>>

    @Query("SELECT * FROM language WHERE name LIKE '%' || :query || '%'")
    fun searchLanguages(query: String): PagingSource<Int, LanguageEntity>

    @Query("DELETE FROM language")
    fun deleteAll()
}