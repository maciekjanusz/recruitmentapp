package dev.mjanusz.recruitmentapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mjanusz.recruitmentapp.data.remote.model.GitHubUserDetailsDto
import java.time.OffsetDateTime

@Entity(tableName = "userDetails")
data class UserDetails(
    @PrimaryKey val login: String,
    val id: Long,
    val avatarUrl: String,
    val company: String,
    val location: String,
    val name: String,
    val email: String,
    val bio: String,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

fun GitHubUserDetailsDto.toUserDetails() =
    UserDetails(
        login = login,
        id = id,
        avatarUrl = avatarUrl,
        company = company ?: "",
        location = location ?: "",
        email = email ?: "",
        bio = bio ?: "",
        name = name ?: "",
        publicRepos = publicRepos,
        publicGists = publicGists,
        followers = followers,
        following = following,
        createdAt = createdAt,
        updatedAt = updatedAt
    )