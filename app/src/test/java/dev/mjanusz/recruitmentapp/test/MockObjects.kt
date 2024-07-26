package dev.mjanusz.recruitmentapp.test

import dev.mjanusz.recruitmentapp.data.local.model.toEntity
import dev.mjanusz.recruitmentapp.data.remote.model.RepositoryDetailsDto
import dev.mjanusz.recruitmentapp.data.remote.model.RepositoryOwnerDto
import dev.mjanusz.recruitmentapp.ui.model.toRepositoryDetails
import java.time.OffsetDateTime

val mockRepositoryDetailsDto = RepositoryDetailsDto(
    id = 764707505,
    name = "recruitmentapp",
    fullName = "maciekjanusz/recruitmentapp",
    private = false,
    htmlUrl = "https://github.com/maciekjanusz/recruitmentapp",
    description = "Lorem ipsum dolor sit amet",
    owner = RepositoryOwnerDto(
        id = 8041839,
        login = "maciekjanusz",
        avatarUrl = "https://avatars.githubusercontent.com/u/8041839?v=4",
        htmlUrl = "https://github.com/maciekjanusz",
        type = "User"
    ),
    createdAt = OffsetDateTime.now(),
    updatedAt = OffsetDateTime.now(),
    pushedAt = OffsetDateTime.now(),
    stargazersCount = 123,
    watchersCount = 12,
    forksCount = 3,
    language = "Kotlin",
)

val mockRepositoryDetailsEntity = mockRepositoryDetailsDto.toEntity()

val mockRepositoryDetails = mockRepositoryDetailsEntity.toRepositoryDetails()