package dev.mjanusz.recruitmentapp.ui.languages

import dev.mjanusz.recruitmentapp.data.GitHubTrendingSource
import dev.mjanusz.recruitmentapp.data.local.model.RepositoryFavView
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class LanguagesPickerViewModelTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockSource = mock<GitHubTrendingSource>()

    private val lazyInitViewModel by lazy {
        LanguagesPickerViewModel(
            mockSource
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test fetch and update languages on init`() = runTest {
        // given
        doReturn(flowOf(emptyList<RepositoryFavView>())).whenever(mockSource).getTrendingRepos()
        // when
        lazyInitViewModel // (init)
        advanceUntilIdle()
        // then
        verify(mockSource).fetchAndUpdateLanguages()
    }
}