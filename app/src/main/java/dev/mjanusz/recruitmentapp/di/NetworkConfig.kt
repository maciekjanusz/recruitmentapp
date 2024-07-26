package dev.mjanusz.recruitmentapp.di

data class NetworkConfig(
    val apiBaseUrl: String = "https://api.github.com/",
    val siteBaseUrl: String = "https://github.com/",
    val apiToken: String = "",
)