package dev.mjanusz.recruitmentapp.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mjanusz.recruitmentapp.data.remote.model.RepositoryDetailsDto
import dev.mjanusz.recruitmentapp.data.remote.model.RepositoryOwnerDto
import kotlinx.serialization.SerialName
import java.time.OffsetDateTime

@Entity(tableName = "repository_details")
data class RepositoryDetailsEntity(
    val id: Long,
    val name: String,
    @PrimaryKey val fullName: String,
    val private: Boolean,
    val htmlUrl: String,
    val description: String?,
    @Embedded val owner: RepositoryOwnerEntity,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val pushedAt: OffsetDateTime,
    val stargazersCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val language: String,
)

data class RepositoryOwnerEntity(
    val login: String,
    val ownerId: Long,
    val avatarUrl: String,
    val ownerHtmlUrl: String,
    val type: String,
)

fun RepositoryOwnerDto.toEntity() = RepositoryOwnerEntity(
    login = login,
    ownerId = id,
    avatarUrl = avatarUrl,
    ownerHtmlUrl = htmlUrl,
    type = type,
)

fun RepositoryDetailsDto.toEntity() = RepositoryDetailsEntity(
    id = id,
    name = name,
    fullName = fullName,
    private = private,
    htmlUrl = htmlUrl,
    description = description,
    owner = owner.toEntity(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    pushedAt = pushedAt,
    stargazersCount = stargazersCount,
    watchersCount = watchersCount,
    forksCount = forksCount,
    language = language,
)