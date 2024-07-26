package dev.mjanusz.recruitmentapp.data.remote.model

import dev.mjanusz.recruitmentapp.data.local.KOffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class RepositoryDetailsDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("private") val private: Boolean,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("owner") val owner: RepositoryOwnerDto,
    @SerialName("description") val description: String?,
    @Serializable(KOffsetDateTimeSerializer::class) @SerialName("created_at") val createdAt: OffsetDateTime,
    @Serializable(KOffsetDateTimeSerializer::class) @SerialName("updated_at") val updatedAt: OffsetDateTime,
    @Serializable(KOffsetDateTimeSerializer::class) @SerialName("pushed_at") val pushedAt: OffsetDateTime,
    @SerialName("stargazers_count") val stargazersCount: Int,
    @SerialName("watchers_count") val watchersCount: Int,
    @SerialName("forks_count") val forksCount: Int,
    @SerialName("language") val language: String,
)

@Serializable
data class RepositoryOwnerDto(
    @SerialName("login") val login: String,
    @SerialName("id") val id: Long,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("type") val type: String,
)
