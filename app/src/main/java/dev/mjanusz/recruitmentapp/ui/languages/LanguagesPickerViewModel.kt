package dev.mjanusz.recruitmentapp.ui.languages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.util.query
import coil.network.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mjanusz.recruitmentapp.data.GitHubTrendingSource
import dev.mjanusz.recruitmentapp.ui.common.ChannelEventHandler
import dev.mjanusz.recruitmentapp.ui.common.UIEventHandler
import dev.mjanusz.recruitmentapp.ui.common.USER_INPUT_DEBOUNCE_MILLIS
import dev.mjanusz.recruitmentapp.ui.model.AnyLanguage
import dev.mjanusz.recruitmentapp.ui.model.RepositoryLanguage
import dev.mjanusz.recruitmentapp.ui.model.toRepositoryLanguage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LanguagesPickerViewModel @Inject constructor(
    private val gitHubTrendingSource: GitHubTrendingSource,
    private val languageClickHandler: UIEventHandler<RepositoryLanguage> = ChannelEventHandler()
) : ViewModel() {

    val languageClicked by languageClickHandler

    private val _selectedLanguage = MutableStateFlow<RepositoryLanguage>(AnyLanguage)
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResults = _searchQuery
        .debounce(timeoutMillis = USER_INPUT_DEBOUNCE_MILLIS)
        .onEach { Log.d("LanguagesViewModel" ,"query: $it") }
        .flatMapLatest { query ->
            gitHubTrendingSource.searchLanguages(query)
                .map { it.map { entity -> entity.toRepositoryLanguage() } }
                .cachedIn(viewModelScope)
        }

    init {
        viewModelScope.launch {
            try {
                gitHubTrendingSource.fetchAndUpdateLanguages()
            } catch (e: IOException) {
                // TODO: error indication
            } catch (e: HttpException) {
                // TODO: error indication
            }
        }
    }

    fun onQueryChange(query: String) {
        _searchQuery.update { query.trim() }
    }

    fun onLanguageClicked(language: RepositoryLanguage) {
        viewModelScope.launch {
            val selectedLang = if(_selectedLanguage.value != language) language else AnyLanguage
            _selectedLanguage.value = selectedLang
            languageClickHandler.onEvent(selectedLang)
        }
    }

}