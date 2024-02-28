package dev.mjanusz.recruitmentapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import dev.mjanusz.recruitmentapp.data.local.dao.UserDao
import dev.mjanusz.recruitmentapp.data.local.model.UserEntity
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import dev.mjanusz.recruitmentapp.data.remote.model.GitHubUserDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class UsersMediatorTest : MockDatabaseTransactionTest() {

    private val pagingConfig = PagingConfig(
        pageSize = GitHubApi.PAGE_SIZE, prefetchDistance = 1
    )
    private val mockDao = mock<UserDao> { }
    private val mockApi = mock<GitHubApi> { }

    private val mediator = UsersMediator(mockApi, mockDao, mockDatabase)


    @Test
    fun `test refresh`() = runTest {
        // given
        val loadType = LoadType.REFRESH
        doReturn(emptyList<GitHubUserDto>()).whenever(mockApi).getUsers(anyOrNull(), anyOrNull())
        // when
        launch {
            val result = mediator.load(
                loadType,
                PagingState(emptyList(), 0, pagingConfig, 0)
            )
            assert(result is MediatorResult.Success && result.endOfPaginationReached)
        }
        advanceUntilIdle()
        // then
        verify(mockApi).getUsers(any<Long>(), any<Int>())
        verify(mockDao).clearAll()
        verify(mockDao).upsertList(any<List<UserEntity>>())
        verifyNoMoreInteractions(mockApi, mockDao)
    }
}