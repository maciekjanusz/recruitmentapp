package dev.mjanusz.recruitmentapp.data

import androidx.paging.PagingSource
import app.cash.turbine.test
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import dev.mjanusz.recruitmentapp.data.local.dao.UserDao
import dev.mjanusz.recruitmentapp.data.local.model.UserEntity
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserListRepositoryTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockPagingSource = mock<PagingSource<Int, UserEntity>>()
    private val mockDao = mock<UserDao> {
        on { getUsersList() }.doReturn(mockPagingSource)
    }
    private val mockApi = mock<GitHubApi> { }
    private val mockDatabase = mock<AppDatabase> { }
    private val repository = UserListRepository(mockDao, mockApi, mockDatabase)

    @Test
    fun `test create response pager`() = runTest {
        // when
        val flow = repository.getUsersPager()
        // then
        flow.test {
            awaitItem()
            verify(mockDao).getUsersList()
        }
    }
}