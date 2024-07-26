package dev.mjanusz.recruitmentapp.ui.model

import dev.mjanusz.recruitmentapp.data.local.model.RepositoryDetailsEntity
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryOwnerEntity
import java.time.OffsetDateTime

data class RepositoryDetails(
    val id: Long,
    val name: String,
    val fullName: String,
    val private: Boolean,
    val htmlUrl: String,
    val description: String?,
    val owner: RepositoryOwner,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val pushedAt: OffsetDateTime,
    val stargazersCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val language: String,
)

data class RepositoryOwner(
    val login: String,
    val id: Long,
    val avatarUrl: String,
    val htmlUrl: String,
    val type: String,
)

fun RepositoryOwnerEntity.toRepositoryOwner() = RepositoryOwner(
    login = login,
    id = ownerId,
    avatarUrl = avatarUrl,
    htmlUrl = ownerHtmlUrl,
    type = type
)

fun RepositoryDetailsEntity.toRepositoryDetails() = RepositoryDetails(
    id = id,
    name = name,
    fullName = fullName,
    private = private,
    htmlUrl = htmlUrl,
    description = description,
    owner = owner.toRepositoryOwner(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    pushedAt = pushedAt,
    stargazersCount = stargazersCount,
    watchersCount = watchersCount,
    forksCount = forksCount,
    language = language,
)
