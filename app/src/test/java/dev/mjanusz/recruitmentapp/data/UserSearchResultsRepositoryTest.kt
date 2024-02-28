package dev.mjanusz.recruitmentapp.data

import androidx.paging.PagingSource
import app.cash.turbine.test
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import dev.mjanusz.recruitmentapp.data.local.dao.UserSearchResultsDao
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import dev.mjanusz.recruitmentapp.test.mockUser
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserSearchResultsRepositoryTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockPagingSource = mock<PagingSource<Int, UserSearchResultEntity>>()
    private val mockDao = mock<UserSearchResultsDao> {
        on { getSearchResults() }.doReturn(mockPagingSource)
    }
    private val mockApi = mock<GitHubApi> { }
    private val mockDatabase = mock<AppDatabase> { }
    private val repository = UserSearchResultsRepository(mockDao, mockApi, mockDatabase)

    @Test
    fun `test create response pager`() = runTest {
        // when
        val flow = repository.getSearchResults(mockUser.login)
        // then
        flow.test {
            awaitItem()
            verify(mockDao).getSearchResults()
        }
    }
}