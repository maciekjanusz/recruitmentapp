package dev.mjanusz.recruitmentapp.data

import dev.mjanusz.recruitmentapp.data.local.dao.RepositoryDetailsDao
import dev.mjanusz.recruitmentapp.data.local.model.toEntity
import dev.mjanusz.recruitmentapp.data.remote.GitHubApi
import dev.mjanusz.recruitmentapp.test.mockRepositoryDetails
import dev.mjanusz.recruitmentapp.test.mockRepositoryDetailsDto
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GitHubRepositorySourceTest {

    private val mockApi = mock<GitHubApi> { }
    private val mockDao = mock<RepositoryDetailsDao> { }

    private val gitHubRepositorySource = GitHubRepositorySource(
        mockApi, mockDao
    )

    @Test
    fun `test get repository details`() {
        // given
        val owner = mockRepositoryDetails.owner.login
        val repo = mockRepositoryDetails.name
        // when
        gitHubRepositorySource.getRepositoryDetails(owner, repo)
        // then
        verify(mockDao).getRepositoryDetails("$owner/$repo")
    }

    @Test
    fun `test fetch and update repository details`() = runTest {
        // given
        val owner = mockRepositoryDetails.owner.login
        val repo = mockRepositoryDetails.name
        doReturn(mockRepositoryDetailsDto).whenever(mockApi)
            .getRepositoryDetails(anyString(), anyString())
        // when
        gitHubRepositorySource.fetchAndUpdateRepositoryDetails(owner, repo)
        // then
        verify(mockApi).getRepositoryDetails(owner, repo)
        verify(mockDao).upsertRepositoryDetails(mockRepositoryDetailsDto.toEntity())
    }
}