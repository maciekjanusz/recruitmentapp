package dev.mjanusz.recruitmentapp.ui.trending

import dev.mjanusz.recruitmentapp.data.GitHubTrendingSource
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryFavView
import dev.mjanusz.recruitmentapp.data.remote.TrendingDateRange
import dev.mjanusz.recruitmentapp.data.remote.model.TrendingReposDto
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import dev.mjanusz.recruitmentapp.ui.common.UiModeHelper
import dev.mjanusz.recruitmentapp.ui.model.AnyLanguage
import dev.mjanusz.recruitmentapp.ui.model.RepositoryLanguage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class TrendingViewModelTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockTrendingSource = mock<GitHubTrendingSource> { }
    private val mockUiHelper = mock<UiModeHelper> { }

    private val lazyInitViewModel by lazy {
        TrendingViewModel(
            mockTrendingSource,
            mockUiHelper
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test fetch and update trending on init`() = runTest {
        // given
        val defaultLanguage = AnyLanguage
        val languageArgCaptor = argumentCaptor<RepositoryLanguage>()
        val rangeArgCaptor = argumentCaptor<TrendingDateRange>()
        val defaultRange = TrendingDateRange.DAILY
        doReturn(flowOf(emptyList<RepositoryFavView>())).whenever(mockTrendingSource).getTrendingRepos()
        // when
        lazyInitViewModel
        advanceUntilIdle()
        // then
        verify(mockTrendingSource).fetchAndUpdateTrendingRepos(
            languageArgCaptor.capture(),
            rangeArgCaptor.capture()
        )
        assertEquals(defaultLanguage, languageArgCaptor.firstValue)
        assertEquals(defaultRange, rangeArgCaptor.firstValue)
    }
}