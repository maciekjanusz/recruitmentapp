package dev.mjanusz.recruitmentapp.ui.users.search

import androidx.paging.PagingData
import app.cash.turbine.test
import dev.mjanusz.recruitmentapp.data.UserSearchResultsRepository
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import dev.mjanusz.recruitmentapp.test.mockUser
import dev.mjanusz.recruitmentapp.ui.common.USER_INPUT_DEBOUNCE_MILLIS
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import kotlin.test.assertEquals

class UserSearchViewModelTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockEmptyPagingData: PagingData<UserSearchResultEntity> = PagingData.empty()
    private val mockRepository = mock<UserSearchResultsRepository> {
        on { getSearchResults(any()) }.doReturn(flowOf(mockEmptyPagingData))
    }
    private val viewModel = UserSearchViewModel(mockRepository)

    @Test
    fun `test search empty query`() = runTest {
        // given
        val query = ""
        // when
        viewModel.onQueryChange(query)
        // then
        viewModel.searchQuery.test {
            assertEquals(query, awaitItem())
        }
        viewModel.searchResults.test {
            awaitItem()
            verify(mockRepository).getSearchResults(query)
        }
        verifyNoMoreInteractions(mockRepository)
    }

    @Test
    fun `test non empty query`() = runTest {
        // given
        val queryArgumentCaptor = argumentCaptor<String>()
        val query = "username"
        // when
        viewModel.onQueryChange(query)
        // then
        viewModel.searchQuery.test {
            assertEquals(query, awaitItem())
        }
        viewModel.searchResults.test {
            awaitItem()
            verify(mockRepository).getSearchResults(queryArgumentCaptor.capture())
            assertEquals(query, queryArgumentCaptor.firstValue)
        }
        verifyNoMoreInteractions(mockRepository)
    }

    @Test
    fun `test query update`() = runTest() {
        val query1 = "user"
        val query2 = "username"
        viewModel.searchQuery.test {
            assertEquals("", awaitItem())

            viewModel.onQueryChange(query1)
            assertEquals(query1, awaitItem())

            viewModel.onQueryChange(query2)
            assertEquals(query2, awaitItem())
        }
    }

    @Test
    fun `test query change with interval over debounce value`() = runTest {
        // given
        val queryArgumentCaptor = argumentCaptor<String>()
        val initialState = ""
        val query1 = "user"
        val query2 = "username"
        val queryList = listOf(query1, query2)
        // when
        viewModel.searchResults.test {
            delay(USER_INPUT_DEBOUNCE_MILLIS)
            queryList.forEach {
                viewModel.onQueryChange(it)
                delay(USER_INPUT_DEBOUNCE_MILLIS)
            }
            repeat(3) { awaitItem() }
            // then
            verify(mockRepository, times(3))
                .getSearchResults(queryArgumentCaptor.capture())
            assertEquals(queryArgumentCaptor.firstValue, initialState)
            assertEquals(queryArgumentCaptor.secondValue, query1)
            assertEquals(queryArgumentCaptor.thirdValue, query2)
            verifyNoMoreInteractions(mockRepository)
        }
    }

    @Test
    fun `test query change with all debounced`() = runTest{
        // given
        val queryArgumentCaptor = argumentCaptor<String>()
        val query1 = "user"
        val query2 = "username"
        val queryList = listOf(query1, query2)
        // when
        viewModel.searchResults.test {
            queryList.forEach {
                viewModel.onQueryChange(it)
            }
            awaitItem()
            // then
            verify(mockRepository, times(1))
                .getSearchResults(queryArgumentCaptor.capture())
            assertEquals(queryArgumentCaptor.firstValue, query2)
            verifyNoMoreInteractions(mockRepository)
        }
    }

    private fun testOnActiveChange(active: Boolean) {
        viewModel.onActiveChange(active)
        // then
        assertEquals(active, viewModel.searchActive.value)
    }

    @Test
    fun `test active change to true`() = runTest {
        // when
        val query = "username"
        viewModel.onQueryChange(query)
        assertEquals(query, viewModel.searchQuery.value)
        testOnActiveChange(true)
        // query state shouldn't change
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `test active change to false`() = runTest {
        // when
        val query = "username"
        viewModel.onQueryChange(query)
        assertEquals(query, viewModel.searchQuery.value)
        testOnActiveChange(false)
        // query state should change to empty
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `test user clicked`() = runTest {
        // given
        // when
        viewModel.onUserClicked(mockUser)
        // then
        viewModel.userClicked.test {
            assertEquals(mockUser, awaitItem())
        }
    }
}