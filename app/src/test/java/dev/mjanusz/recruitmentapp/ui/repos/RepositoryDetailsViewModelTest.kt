package dev.mjanusz.recruitmentapp.ui.repos

import dev.mjanusz.recruitmentapp.data.GitHubRepositorySource
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import dev.mjanusz.recruitmentapp.test.mockRepositoryDetails
import dev.mjanusz.recruitmentapp.test.mockRepositoryDetailsEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RepositoryDetailsViewModelTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val owner = "maciekjanusz"
    private val repo = "recruitmentapp"
    private val mockSource = mock<GitHubRepositorySource>()

    private val lazyInitViewModel by lazy {
        RepositoryDetailsViewModel(
            owner, repo, mockSource
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test fetch on init`() = runTest {
        // when
        doReturn(flowOf(mockRepositoryDetailsEntity)).whenever(mockSource)
            .getRepositoryDetails(anyString(), anyString())
        // given
        lazyInitViewModel
        advanceUntilIdle()
        // then
        verify(mockSource).fetchAndUpdateRepositoryDetails(owner, repo)
    }
}