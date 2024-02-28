package dev.mjanusz.recruitmentapp.data

import app.cash.turbine.test
import dev.mjanusz.recruitmentapp.common.Failure
import dev.mjanusz.recruitmentapp.common.LoadingState
import dev.mjanusz.recruitmentapp.data.local.dao.UserDetailsDao
import dev.mjanusz.recruitmentapp.data.local.model.UserDetails
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import dev.mjanusz.recruitmentapp.test.mockUser
import dev.mjanusz.recruitmentapp.test.mockUserDetails
import dev.mjanusz.recruitmentapp.test.mockUserDetailsDto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserDetailsRepositoryTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockApi = mock<GitHubApi> { }
    private val mockDao = mock<UserDetailsDao> { }
    private val repository = UserDetailsRepository(mockApi, mockDao)

    @Test
    fun `test get user details with successful remote fetch`() = runTest {
        // given
        val insertArgumentCaptor = argumentCaptor<UserDetails>()
        val mockDaoFlow = MutableSharedFlow<UserDetails>(replay = 1) // simulate non-completing flow
        mockDaoFlow.emit(mockUserDetails)
        doReturn(mockUserDetailsDto).whenever(mockApi).getUserDetails(mockUser.login)
        doReturn(mockDaoFlow).whenever(mockDao).getUserDetails(mockUser.login)
        // when
        val flow = repository.getUserDetails(mockUser.login)
        // then
        flow.test {
            // initial loading emission
            assertEquals(LoadingState.loading(), awaitItem())
            assertEquals(LoadingState.success(mockUserDetails), awaitItem())
        }
        verify(mockDao).getUserDetails(mockUserDetails.login)
        verify(mockDao).insertUserDetails(insertArgumentCaptor.capture())
        assertEquals(mockUserDetails, insertArgumentCaptor.firstValue)
        verifyNoMoreInteractions(mockDao)
    }

    @Test
    fun `test get user details with empty cache and unsuccessful remote fetch`() = runTest {
        // given
        val errorMessage = "simulated exception"
        val mockDaoFlow = MutableSharedFlow<UserDetails>(replay = 1) // simulate non-completing flow
        // do not emit any items to mockDaoFlow
        doThrow(RuntimeException(errorMessage)).whenever(mockApi).getUserDetails(mockUser.login)
        doReturn(mockDaoFlow).whenever(mockDao).getUserDetails(mockUser.login)
        // when
        val flow = repository.getUserDetails(mockUser.login)
        // then
        flow.test {
            assertEquals(LoadingState.loading(), awaitItem())
            assertTrue(awaitItem() is Failure<UserDetails>)
        }
        verify(mockDao, never()).insertUserDetails(any())
    }
}