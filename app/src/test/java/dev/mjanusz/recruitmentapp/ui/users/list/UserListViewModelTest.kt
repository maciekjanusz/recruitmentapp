package dev.mjanusz.recruitmentapp.ui.users.list

import androidx.paging.PagingData
import app.cash.turbine.test
import dev.mjanusz.recruitmentapp.data.UserListRepository
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import dev.mjanusz.recruitmentapp.test.mockUser
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class UserListViewModelTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockRepository = mock<UserListRepository> {
        on { getUsersPager() }.doReturn(flowOf(PagingData.empty()))
    }
    private val viewModel by lazy { UserListViewModel(mockRepository) }

    @Test
    fun `test get users pager on init`() {
        // when
        // necessary to initialize lazy
        viewModel
        // then
        verify(mockRepository).getUsersPager()
        verifyNoMoreInteractions(mockRepository)
    }

    @Test
    fun `test user clicked`() = runTest {
        // when
        viewModel.onUserClicked(mockUser)
        // then
        viewModel.userClicked.test {
            assertEquals(mockUser, awaitItem())
        }
    }
}