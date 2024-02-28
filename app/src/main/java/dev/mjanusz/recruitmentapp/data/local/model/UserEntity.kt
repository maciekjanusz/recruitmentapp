package dev.mjanusz.recruitmentapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mjanusz.recruitmentapp.data.remote.model.GitHubUserDto

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val login: String,
    val avatarUrl: String
)

fun GitHubUserDto.toUserEntity() =
    UserEntity(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )