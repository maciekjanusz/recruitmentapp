package dev.mjanusz.recruitmentapp.ui.users.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mjanusz.recruitmentapp.data.UserListRepository
import dev.mjanusz.recruitmentapp.ui.common.UIEventHandler
import dev.mjanusz.recruitmentapp.ui.common.ChannelEventHandler
import dev.mjanusz.recruitmentapp.ui.model.User
import dev.mjanusz.recruitmentapp.ui.model.toUser
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    userListRepository: UserListRepository,
    private val userClickHandler: UIEventHandler<User> = ChannelEventHandler()
) : ViewModel() {

    val userClicked by userClickHandler

    val userPagingFlow = userListRepository
        .getUsersPager()
        .map { it.map { userEntity -> userEntity.toUser() } }
        .cachedIn(viewModelScope)

    fun onUserClicked(user: User) {
        viewModelScope.launch {
            userClickHandler.onEvent(user)
        }
    }
}