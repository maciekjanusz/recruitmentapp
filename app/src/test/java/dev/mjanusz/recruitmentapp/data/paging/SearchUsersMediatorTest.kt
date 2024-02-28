package dev.mjanusz.recruitmentapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import dev.mjanusz.recruitmentapp.data.local.dao.UserSearchResultsDao
import dev.mjanusz.recruitmentapp.data.local.model.UserSearchResultEntity
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import dev.mjanusz.recruitmentapp.data.remote.model.UserSearchDto
import dev.mjanusz.recruitmentapp.test.mockUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class SearchUsersMediatorTest : MockDatabaseTransactionTest() {

    private val pagingConfig = PagingConfig(
        pageSize = GitHubApi.PAGE_SIZE, prefetchDistance = 1
    )
    private val mockDao = mock<UserSearchResultsDao> { }
    private val mockApi = mock<GitHubApi> { }
    private val query = mockUser.login

    private val mediator = SearchUsersMediator(mockApi, mockDao, mockDatabase, query)

    @Test
    fun `test refresh on empty paging state`() = runTest {
        // given
        val loadType = LoadType.REFRESH
        doReturn(UserSearchDto(1, false, emptyList()))
            .whenever(mockApi)
            .searchUsers(any<String>(), any<Int>(), anyOrNull<String>(), any<String>(), any<Int>())
        // when
        launch {
            val result = mediator.load(
                loadType, PagingState(emptyList(), 0, pagingConfig, 0)
            )
            // then
            assert(result is MediatorResult.Success && !result.endOfPaginationReached)
        }
        advanceUntilIdle()
        // then
        verify(mockDao).clearAll()
        verify(mockApi).searchUsers(query = query, page = SearchUsersMediator.INITIAL_KEY + 1)
        verify(mockDao).upsertList(any<List<UserSearchResultEntity>>())
        verifyNoMoreInteractions(mockDao, mockApi)
    }

    @Test
    fun `test prepend`() = runTest {
        // given
        val loadType = LoadType.PREPEND
        // when
        launch {
            val result = mediator.load(
                loadType, PagingState(emptyList(), 0, pagingConfig, 0)
            )
            // then
            assert(result is MediatorResult.Success && result.endOfPaginationReached)
        }
        advanceUntilIdle()
        // then
        verifyNoInteractions(mockDao, mockApi)
    }

    @Test
    fun `test append`() = runTest {
        // given
        val loadType = LoadType.APPEND
        doReturn(UserSearchDto(1, false, emptyList()))
            .whenever(mockApi)
            .searchUsers(any<String>(), any<Int>(), anyOrNull<String>(), any<String>(), any<Int>())
        // when
        launch {
            val result = mediator.load(
                loadType, PagingState(emptyList(), 0, pagingConfig, 0)
            )
            // then
            assert(result is MediatorResult.Success && !result.endOfPaginationReached)
        }
        advanceUntilIdle()
        // then
        verify(mockApi).searchUsers(query = query, page = SearchUsersMediator.INITIAL_KEY + 2)
        verify(mockDao).upsertList(any<List<UserSearchResultEntity>>())
        verifyNoMoreInteractions(mockDao, mockApi)
    }

    @Test
    fun `test load error on append`() = runTest {
        // given
        val loadType = LoadType.APPEND
        val mockResponseBody = mock<ResponseBody>()
        // todo: not working???
        doThrow(HttpException(Response.error<UserSearchDto>(500, mockResponseBody)))
            .whenever(mockApi).searchUsers(any<String>(), any<Int>(), anyOrNull<String>(), any<String>(), any<Int>())
        // when
        launch {
            val result = mediator.load(
                loadType, PagingState(emptyList(), 0, pagingConfig, 0)
            )
            // then
            assert(result is MediatorResult.Error)
        }
        advanceUntilIdle()
        // then
        verify(mockApi).searchUsers(query = query, page = SearchUsersMediator.INITIAL_KEY + 2)
        verifyNoMoreInteractions(mockApi)
        verifyNoInteractions(mockDao)
    }


}