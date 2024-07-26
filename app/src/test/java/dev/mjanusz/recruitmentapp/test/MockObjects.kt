package dev.mjanusz.recruitmentapp.test

import dev.mjanusz.recruitmentapp.data.local.model.UserDetailsEntity
import dev.mjanusz.recruitmentapp.data.local.model.toEntity
import dev.mjanusz.recruitmentapp.data.remote.model.GitHubUserDetailsDto
import dev.mjanusz.recruitmentapp.data.remote.model.RepositoryDetailsDto
import dev.mjanusz.recruitmentapp.data.remote.model.RepositoryOwnerDto
import dev.mjanusz.recruitmentapp.ui.model.User
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

val mockUser = User(0, "username", "mockUrl")

val mockUserDetails = UserDetailsEntity(
    login = mockUser.login,
    id = mockUser.id,
    avatarUrl = mockUser.avatarUrl,
    company = "company",
    location = "location",
    email = "user@host.domain",
    bio = "bio bio bio",
    name = "user",
    publicRepos = 0,
    publicGists = 0,
    followers = 0,
    following = 0,
    createdAt = OffsetDateTime.parse("2024-02-25T17:55:43+01:00"),
    updatedAt = OffsetDateTime.parse("2024-02-26T17:55:43+01:00")
)

val mockUserDetailsDto = GitHubUserDetailsDto(
    login = mockUser.login,
    id = mockUser.id,
    avatarUrl = mockUser.avatarUrl,
    company = "company",
    location = "location",
    email = "user@host.domain",
    bio = "bio bio bio",
    name = "user",
    publicRepos = 0,
    publicGists = 0,
    followers = 0,
    following = 0,
    createdAt = OffsetDateTime.parse("2024-02-25T17:55:43+01:00"),
    updatedAt = OffsetDateTime.parse("2024-02-26T17:55:43+01:00")
)