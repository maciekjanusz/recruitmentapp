package dev.mjanusz.recruitmentapp.data

import dev.mjanusz.recruitmentapp.data.local.dao.FavouritesDao
import dev.mjanusz.recruitmentapp.data.local.dao.LanguageDao
import dev.mjanusz.recruitmentapp.data.local.dao.RepositoriesDao
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryEntity
import dev.mjanusz.recruitmentapp.data.paging.MockDatabaseTransactionTest
import dev.mjanusz.recruitmentapp.data.remote.GithubTrendingApi
import dev.mjanusz.recruitmentapp.data.remote.TrendingDateRange
import dev.mjanusz.recruitmentapp.data.remote.model.TrendingReposDto
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import dev.mjanusz.recruitmentapp.ui.model.SpecificLanguage
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

class GitHubTrendingSourceTest : MockDatabaseTransactionTest() {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockApi = mock<GithubTrendingApi> { }
    private val mockDao = mock<RepositoriesDao> { }
    private val mockLanguagesDao = mock<LanguageDao> { }
    private val mockFavouritesDao = mock<FavouritesDao> { }

    private val trendingSource = GitHubTrendingSource(
        mockApi, mockDao, mockLanguagesDao, mockFavouritesDao, mockDatabase
    )

    @Test
    fun `test fetch and update repos`() = runTest {
        // given
        val entitiesCaptor = argumentCaptor<List<RepositoryEntity>>()
        val mockDto = TrendingReposDto().apply { repositories = emptyList() }
        val langName = "Kotlin"
        val urlPath = "kotlin"
        val language = SpecificLanguage(langName, urlPath)
        val dateRange = TrendingDateRange.DAILY
        doReturn(mockDto).whenever(mockApi)
            .getTrendingRepos(urlPath, dateRange)
        // when
        trendingSource.fetchAndUpdateTrendingRepos(language, dateRange)
        // then
        verify(mockApi).getTrendingRepos(urlPath, dateRange)
        verify(mockDao).deleteAll()
        verify(mockDao).insertRepositories(entitiesCaptor.capture())
        assertTrue { entitiesCaptor.firstValue.isEmpty() }
    }
}