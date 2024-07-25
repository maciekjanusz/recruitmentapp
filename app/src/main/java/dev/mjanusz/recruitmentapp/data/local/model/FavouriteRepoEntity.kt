package dev.mjanusz.recruitmentapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_repo")
data class FavouriteRepoEntity(
    @PrimaryKey val repoUrl: String
)
