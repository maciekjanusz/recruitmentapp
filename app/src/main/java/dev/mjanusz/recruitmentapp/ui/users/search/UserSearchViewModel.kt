package dev.mjanusz.recruitmentapp.ui.users.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mjanusz.recruitmentapp.data.UserSearchResultsRepository
import dev.mjanusz.recruitmentapp.ui.common.UIEventHandler
import dev.mjanusz.recruitmentapp.ui.common.USER_INPUT_DEBOUNCE_MILLIS
import dev.mjanusz.recruitmentapp.ui.common.ChannelEventHandler
import dev.mjanusz.recruitmentapp.ui.model.User
import dev.mjanusz.recruitmentapp.ui.model.toUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val searchResultsRepository: UserSearchResultsRepository,
    private val userClickHandler: UIEventHandler<User> = ChannelEventHandler()
) : ViewModel() {

    val userClicked by userClickHandler

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchActive = MutableStateFlow(false)
    val searchActive = _searchActive.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResults = _searchQuery
        .debounce(timeoutMillis = USER_INPUT_DEBOUNCE_MILLIS)
        .flatMapLatest { query ->
            searchResultsRepository.getSearchResults(query)
                .map { it.map { entity -> entity.toUser() } }
                .cachedIn(viewModelScope)
        }

    fun onQueryChange(query: String) {
        _searchQuery.update { query.trim() }
    }

    fun onActiveChange(active: Boolean) {
        _searchActive.update { active }
        if(!active) {
            _searchQuery.update { "" }
        }
    }

    fun onUserClicked(user: User) {
        viewModelScope.launch {
            userClickHandler.onEvent(user)
        }
    }
}