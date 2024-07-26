package dev.mjanusz.recruitmentapp.data.remote.model

import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector

class LanguagesDto {
    @Selector("#languages-menuitems a")
    lateinit var languages: List<Element>
}

data class LanguageDto(
    val name: String,
    val href: String
)

fun LanguagesDto.toLanguageList() = languages.toList().map { LanguageDto(
    name = it.text(),
    href = it.attr("href")
) }