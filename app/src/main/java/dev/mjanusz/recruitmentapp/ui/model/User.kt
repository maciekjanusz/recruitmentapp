package dev.mjanusz.recruitmentapp.ui.model

import dev.mjanusz.recruitmentapp.data.local.model.UserEntity
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity

data class User(
    val id: Long,
    val login: String,
    val avatarUrl: String
)

fun UserEntity.toUser() = User(id, login, avatarUrl)
fun UserSearchResultEntity.toUser() = User(id, login, avatarUrl)