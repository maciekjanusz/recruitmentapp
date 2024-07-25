package dev.mjanusz.recruitmentapp.data.local.model

import androidx.room.DatabaseView

@DatabaseView("SELECT repository.rank, repository.owner, repository.repo, repository.url, repository.description, " +
        "repository.language, repository.languageColor, repository.starsCount, " +
        "repository.forksCount, EXISTS(SELECT * FROM favourite_repo WHERE repoUrl = repository.url) AS favourite FROM repository",
    viewName = "repository_fav_view")
data class RepositoryFavView(
    val rank: Int,
    val owner: String,
    val repo: String,
    val url: String,
    val description: String?,
    val language: String?,
    val languageColor: Int?,
    val starsCount: Int,
    val forksCount: Int,
    val favourite: Boolean
)