package dev.mjanusz.recruitmentapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mjanusz.recruitmentapp.data.remote.model.GitHubUserDto

@Entity(tableName = "userSearches")
data class UserSearchResultEntity(
    val id: Long,
    @PrimaryKey val login: String,
    val avatarUrl: String
)

fun GitHubUserDto.toUserSearchResultEntity() =
    UserSearchResultEntity(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )