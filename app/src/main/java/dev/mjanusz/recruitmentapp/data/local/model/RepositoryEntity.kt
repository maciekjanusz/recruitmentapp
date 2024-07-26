package dev.mjanusz.recruitmentapp.data.local.model

import android.graphics.Color
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.util.Log
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.mjanusz.recruitmentapp.data.remote.model.TrendingReposDto
import java.util.Locale

@Entity(tableName = "repository")
data class RepositoryEntity(
    val rank: Int,
    val owner: String,
    val repo: String,
    @PrimaryKey val url: String,
    val description: String?,
    val language: String?,
    val languageColor: Int?,
    val starsCount: Int,
    val forksCount: Int,
)

fun TrendingReposDto.toEntities() =
    repositories?.mapIndexed { i, v ->
        val numberFormat: DecimalFormat = NumberFormat.getNumberInstance(Locale.ENGLISH) as DecimalFormat
        val ownerAndRepo = v.handle
            .split("/")
            .map{it.trim()}

        val colorString = v.colorElement?.attr("style")
            ?.substringAfterLast(" ")

        RepositoryEntity(
            rank = i,
            owner = ownerAndRepo.first(),
            repo = ownerAndRepo.last(),
            url = v.urlElement.attr("href"),
            description = v.description,
            language = v.language,
            languageColor = try { colorString?.let { Color.parseColor(colorString) } } catch (e: Exception) { null },
            starsCount = numberFormat.parse(v.stars).toInt(),
            forksCount = numberFormat.parse(v.forks).toInt()
        )
    }.orEmpty()