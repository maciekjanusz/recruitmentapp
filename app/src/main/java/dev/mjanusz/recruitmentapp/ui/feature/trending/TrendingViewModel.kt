package dev.mjanusz.recruitmentapp.ui.feature.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mjanusz.recruitmentapp.data.GitHubTrendingSource
import dev.mjanusz.recruitmentapp.data.remote.TrendingDateRange
import dev.mjanusz.recruitmentapp.ui.common.ChannelEventHandler
import dev.mjanusz.recruitmentapp.ui.common.Action
import dev.mjanusz.recruitmentapp.ui.common.UIEventHandler
import dev.mjanusz.recruitmentapp.ui.common.UiModeHelper
import dev.mjanusz.recruitmentapp.ui.model.AnyLanguage
import dev.mjanusz.recruitmentapp.ui.model.Repository
import dev.mjanusz.recruitmentapp.ui.model.RepositoryLanguage
import dev.mjanusz.recruitmentapp.ui.model.toRepository
import dev.mjanusz.recruitmentapp.ui.common.LoadingState.IDLE
import dev.mjanusz.recruitmentapp.ui.common.LoadingState.LOADING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val gitHubTrendingSource: GitHubTrendingSource,
    private val uiModeHelper: UiModeHelper,
    private val repoClickHandler: UIEventHandler<Repository> = ChannelEventHandler(),
    private val actionEventHandler: UIEventHandler<Action> = ChannelEventHandler()
) : ViewModel() {

    private val _trendingRepos = MutableStateFlow<List<Repository>>(emptyList())
    val trendingRepos = _trendingRepos.asStateFlow()

    private val _selectedLanguage = MutableStateFlow<RepositoryLanguage>(AnyLanguage)
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _selectedDateRange = MutableStateFlow(TrendingDateRange.DAILY)
    val selectedDateRange = _selectedDateRange.asStateFlow()

    private val _loadingStates = MutableStateFlow(IDLE)
    val loadingState = _loadingStates.asStateFlow()

    val repositoryClicked by repoClickHandler
    val actionClicked by actionEventHandler

    init {
        viewModelScope.run {
            launch {
                selectedLanguage.combine(selectedDateRange) { language, dateRange ->
                    language to dateRange
                }.onEach { pair ->
                    _loadingStates.value = LOADING
                    try {
                        gitHubTrendingSource.fetchAndUpdateTrendingRepos(pair.first, pair.second)
                    } catch (e: IOException) {
                        // TODO: error indication
                    } catch (e: HttpException) {
                        // TODO: error indication
                    }
                    _loadingStates.value = IDLE
                }.collect()
            }
            launch {
                gitHubTrendingSource.getTrendingRepos()
                    .map { list -> list.map { it.toRepository() } }
                    .onEach { _trendingRepos.emit(it) }
                    .collect()
            }
        }
    }

    fun onRepositoryClicked(repository: Repository) {
        viewModelScope.launch {
            repoClickHandler.onEvent(repository)
        }
    }

    fun onRepositoryFavClicked(repository: Repository) {
        viewModelScope.launch {
            gitHubTrendingSource.toggleFav(repository.url, !repository.favourite)
        }
    }

    fun onActionClicked(action: Action) {
        viewModelScope.launch {
            actionEventHandler.onEvent(action)
        }
    }

    fun onLanguageSelected(language: RepositoryLanguage) {
        viewModelScope.launch {
            _selectedLanguage.value = language
        }
    }

    fun onDateRangeSelected(trendingDateRange: TrendingDateRange) {
        viewModelScope.launch {
            _selectedDateRange.value = trendingDateRange
        }
    }

    fun onSetUiMode(uiMode: UiModeHelper.UiMode) {
        uiModeHelper.setUiMode(uiMode)
    }
}
