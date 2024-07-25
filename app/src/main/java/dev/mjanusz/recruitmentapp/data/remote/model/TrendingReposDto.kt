package dev.mjanusz.recruitmentapp.data.remote.model

import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector

class TrendingReposDto {
    @Selector("article") var repositories: List<TrendingRepoArticleDto>? = null
}

class TrendingRepoArticleDto {
    @Selector("article > h2") lateinit var handle: String
    @Selector("article > h2 > a.Link") lateinit var urlElement: Element
    @Selector("article > p") var description: String? = null
    @Selector("article span.repo-language-color") var colorElement: Element? = null
    @Selector("article span[itemprop=programmingLanguage]") var language: String? = null
    @Selector("article a[href$=stargazers]") lateinit var stars: String
    @Selector("article a[href$=forks]") lateinit var forks: String
}