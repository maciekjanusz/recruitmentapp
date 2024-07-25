package dev.mjanusz.recruitmentapp.ui.model

import dev.mjanusz.recruitmentapp.data.local.model.LanguageEntity

// TODO fix

sealed class RepositoryLanguage(
    val name: String,
    val urlPath: String
)

data object AnyLanguage : RepositoryLanguage(
    name = "Any",
    urlPath = ""
)

class SpecificLanguage(
    name: String,
    urlPath: String
): RepositoryLanguage(
    name = name,
    urlPath = urlPath
)

fun LanguageEntity.toRepositoryLanguage(): RepositoryLanguage = SpecificLanguage(name, urlPath)