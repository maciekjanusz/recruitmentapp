package dev.mjanusz.recruitmentapp.ui.users.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mjanusz.recruitmentapp.common.LoadingState
import dev.mjanusz.recruitmentapp.data.UserDetailsRepository
import dev.mjanusz.recruitmentapp.data.local.model.UserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDetailsRepository: UserDetailsRepository
) : ViewModel() {

    private val _userDetails = MutableStateFlow<LoadingState<UserDetails>>(LoadingState.loading())
    val userDetails = _userDetails.asStateFlow()

    fun loadUserDetails(username: String) {
        viewModelScope.launch {
            userDetailsRepository.getUserDetails(username)
                .onEach { Log.d("UserViewModel", "Event: $it") }
                .catch { e -> emit(LoadingState.failure(e)) }
                .onEach { _userDetails.emit(it) }
                .collect()
        }
    }
}