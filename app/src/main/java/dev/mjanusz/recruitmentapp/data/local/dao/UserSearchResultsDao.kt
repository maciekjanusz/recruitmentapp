package dev.mjanusz.recruitmentapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity

@Dao
interface UserSearchResultsDao {

    @Upsert
    suspend fun upsertList(users: List<UserSearchResultEntity>)

    @Query("SELECT * FROM userSearches WHERE login LIKE '%' || :query || '%'")
    fun getSearchResults(query: String): PagingSource<Int, UserSearchResultEntity>

    @Query("SELECT * FROM userSearches")
    fun getSearchResults(): PagingSource<Int, UserSearchResultEntity>

    @Query("DELETE FROM userSearches")
    suspend fun clearAll()
}
