package dev.mjanusz.recruitmentapp.ui.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mjanusz.recruitmentapp.data.GitHubRepositorySource
import dev.mjanusz.recruitmentapp.ui.common.LoadingState.IDLE
import dev.mjanusz.recruitmentapp.ui.common.LoadingState.LOADING
import dev.mjanusz.recruitmentapp.ui.model.RepositoryDetails
import dev.mjanusz.recruitmentapp.ui.model.toRepositoryDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RepositoryDetailsViewModel @Inject constructor(
    @Named("owner") owner: String,
    @Named("repo") repo: String,
    gitHubRepositorySource: GitHubRepositorySource,
) : ViewModel() {

    private val _repositoryDetails = MutableStateFlow<RepositoryDetails?>(null)
    val repositoryDetails = _repositoryDetails.asStateFlow()

    private val _loadingStates = MutableStateFlow(IDLE)
    val loadingStates = _loadingStates.asStateFlow()

    init {
        viewModelScope.run {
            launch {
                _loadingStates.value = LOADING
                try {
                    gitHubRepositorySource.fetchAndUpdateRepositoryDetails(owner, repo)
                } catch (e: IOException) {
                    // TODO: error indication
                } catch (e: HttpException) {
                    // TODO: error indication
                }
                _loadingStates.value = IDLE
            }
            launch {
                gitHubRepositorySource.getRepositoryDetails(owner, repo)
                    .filterNotNull()
                    .map { it.toRepositoryDetails() }
                    .onEach { _repositoryDetails.emit(it) }
                    .collect()
            }
        }
    }

}