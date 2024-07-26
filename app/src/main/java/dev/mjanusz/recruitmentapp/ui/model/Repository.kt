package dev.mjanusz.recruitmentapp.ui.model

import androidx.compose.ui.graphics.Color
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryFavView

data class Repository(
    val rank: Int,
    val owner: String,
    val repo: String,
    val url: String,
    val description: String?,
    val language: String?,
    val languageColor: Color?,
    val starsCount: Int,
    val forksCount: Int,
    val favourite: Boolean,
)

fun RepositoryFavView.toRepository() = Repository(
    rank = rank,
    owner = owner,
    repo = repo,
    url = url,
    description = description,
    language = language,
    languageColor = languageColor?.let { Color(languageColor) },
    starsCount = starsCount,
    forksCount = forksCount,
    favourite = favourite,
)
