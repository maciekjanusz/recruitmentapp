package dev.mjanusz.recruitmentapp.ui.users.details

import dev.mjanusz.recruitmentapp.data.UserDetailsRepository
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import dev.mjanusz.recruitmentapp.test.mockUser
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class UserViewModelTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val mockRepository = mock<UserDetailsRepository>()
    private val viewModel = UserViewModel(mockRepository)

    @Test
    fun `test load user details`() {
        // given
        val username = mockUser.login
        // when
        viewModel.loadUserDetails(username)
        // then

    }
}