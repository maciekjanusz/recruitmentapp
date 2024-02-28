package dev.mjanusz.recruitmentapp.data.remote.model

import dev.mjanusz.recruitmentapp.data.local.KOffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class GitHubUserDto(
    val login: String,
    val id: Long,
    @SerialName("avatar_url") val avatarUrl: String
)

@Serializable
data class GitHubUserDetailsDto(
    val login: String,
    val id: Long,
    @SerialName("avatar_url") val avatarUrl: String,
    val company: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val name: String?,
    @SerialName("public_repos") val publicRepos: Int,
    @SerialName("public_gists") val publicGists: Int,
    val followers: Int,
    val following: Int,
    @Serializable(KOffsetDateTimeSerializer::class)
    @SerialName("updated_at") val createdAt: OffsetDateTime,
    @Serializable(KOffsetDateTimeSerializer::class)
    @SerialName("created_at") val updatedAt: OffsetDateTime,
)

/*
"name": "monalisa octocat",
  "company": "GitHub",
  "blog": "https://github.com/blog",
  "location": "San Francisco",
  "email": "octocat@github.com",
  "hireable": false,
  "bio": "There once was...",
  "twitter_username": "monatheoctocat",
  "public_repos": 2,
  "public_gists": 1,
  "followers": 20,
  "following": 0,
  "created_at": "2008-01-14T04:33:35Z",
  "updated_at": "2008-01-14T04:33:35Z"
 */