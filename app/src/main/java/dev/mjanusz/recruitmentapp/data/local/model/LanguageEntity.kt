package dev.mjanusz.recruitmentapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mjanusz.recruitmentapp.data.remote.model.LanguageDto

@Entity(tableName = "language")
data class LanguageEntity(
    @PrimaryKey val name: String,
    val urlPath: String,
)

fun LanguageDto.toEntity() = LanguageEntity(
    name = name,
    urlPath = href.substringBefore("?").substringAfterLast("/")
)