package dev.mjanusz.recruitmentapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSearchDto(
    @SerialName("total_count") val totalCount: Long,
    @SerialName("incomplete_results") val incompleteResults: Boolean,
    val items: List<GitHubUserDto>
)