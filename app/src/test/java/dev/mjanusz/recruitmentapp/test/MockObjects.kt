package dev.mjanusz.recruitmentapp.test

import dev.mjanusz.recruitmentapp.data.local.model.UserDetailsEntity
import dev.mjanusz.recruitmentapp.data.remote.model.GitHubUserDetailsDto
import dev.mjanusz.recruitmentapp.ui.model.User
import java.time.OffsetDateTime

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