package dev.mjanusz.recruitmentapp.ui.feature.languages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import dev.mjanusz.recruitmentapp.AppState
import dev.mjanusz.recruitmentapp.R
import dev.mjanusz.recruitmentapp.ui.common.USER_INPUT_DEBOUNCE_MILLIS
import dev.mjanusz.recruitmentapp.ui.model.AnyLanguage
import dev.mjanusz.recruitmentapp.ui.model.RepositoryLanguage
import dev.mjanusz.recruitmentapp.ui.theme.AppTheme
import dev.mjanusz.recruitmentapp.ui.theme.AppTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun LanguagePickerScreen(
    onLanguageSelected: (RepositoryLanguage) -> Unit = {},
    viewModel: LanguagesPickerViewModel = hiltViewModel()
) {
    // state
    val selectedItem by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    // items
    val users = viewModel.searchResults.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.languageClicked.collect {
            onLanguageSelected(it)
        }
    }

    LanguagesPickerContent(
        searchQuery = query,
        active = true,
        items = users,
        selectedItem = selectedItem,
        onQueryChange = viewModel::onQueryChange,
        onItemClicked = viewModel::onLanguageClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LanguagesPickerContent(
    searchQuery: String,
    active: Boolean,
    items: LazyPagingItems<RepositoryLanguage>,
    selectedItem: RepositoryLanguage,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit = { },
    onSearch: (String) -> Unit = { },
    onActiveChange: (Boolean) -> Unit = { },
    onItemClicked: (RepositoryLanguage) -> Unit = { },
) {
    SearchBar(
        modifier = modifier.wrapContentHeight(),
        query = searchQuery,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_placeholder), style =
                AppTypography.bodyLarge
            )
        },
        trailingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(id = R.string.cd_search)
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            dividerColor = MaterialTheme.colorScheme.onSecondaryContainer,
            inputFieldColors = TextFieldDefaults.colors()
        ),
    ) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(
            state = listState
        ) {
            stickyHeader {
                // pinned selected item
                if (selectedItem != AnyLanguage) {
                    LanguageItem(
                        item = selectedItem,
                        modifier = Modifier.animateItemPlacement(),
                        selected = true,
                        itemClickListener = {
                            coroutineScope.launch {
                                delay(USER_INPUT_DEBOUNCE_MILLIS)
                                onItemClicked(it)
                            }
                        }
                    )
                }
            }
            items(
                count = items.itemCount,
                key = items.itemKey { it.urlPath },
            ) { index ->
                items[index]?.run {
                    if (this.urlPath != selectedItem.urlPath) {
                        LanguageItem(
                            modifier = Modifier.animateItemPlacement(),
                            item = this,
                            selected = false,
                            itemClickListener = {
                                coroutineScope.launch {
                                    delay(USER_INPUT_DEBOUNCE_MILLIS)
                                    onItemClicked(it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageItem(
    item: RepositoryLanguage,
    selected: Boolean,
    modifier: Modifier,
    itemClickListener: (RepositoryLanguage) -> Unit = { }
) {
    ListItem(
        modifier = modifier.clickable { itemClickListener(item) },
        headlineContent = {
            Text(
                text = item.name,
                style = AppTypography.bodySmall
            )
        },
        leadingContent = {
            RadioButton(
                selected = selected,
                onClick = { }
            )
        })
}

@Composable
@Preview
fun PreviewLanguagesSearchBarActive() {
    AppTheme {
        LanguagesPickerContent(
            searchQuery = "kotlin",
            active = true,
            selectedItem = AnyLanguage,
            items = emptyFlow<PagingData<RepositoryLanguage>>().collectAsLazyPagingItems()
        )
    }
}

@Composable
@Preview
fun PreviewLanguagesSearchBarInactive() {
    AppTheme {
        LanguagesPickerContent(
            searchQuery = "kotlin",
            active = false,
            selectedItem = AnyLanguage,
            items = emptyFlow<PagingData<RepositoryLanguage>>().collectAsLazyPagingItems()
        )
    }
}
//
//@Composable
//private fun LanguageList(
//    items: List<RepositoryLanguage>,
//    selectedItem: RepositoryLanguage,
//    onLanguageSelected: (RepositoryLanguage) -> Unit = { }
//) {
//    LazyColumn {
//        item {
//            LanguageItem(item = AnyLanguage, selected = selectedItem.urlPath == AnyLanguage.urlPath)
//        }
//        items(
//            count = items.size,
//            key = null
//        ) { index ->
//            items[index].run {
//                LanguageItem(
//                    item = this,
//                    itemClickListener = onLanguageSelected,
//                    selected = selectedItem.urlPath == this.urlPath
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun PreviewLanguageList() {
//    AppTheme {
//        LanguageList(
//            items = listOf(
//                SpecificLanguage("Kotlin", "kotlin"),
//                SpecificLanguage("Java", "java")
//            ),
//            selectedItem = SpecificLanguage("Kotlin", "kotlin")
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LanguageSearchBar(
//    query: String,
//    onQueryChange: (String) -> Unit = { },
//    items: List<RepositoryLanguage>,
//    itemClickListener: (RepositoryLanguage) -> Unit = { }
//) {
//    Column {
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            text = stringResource(id = R.string.select_language),
//            style = AppTypography.titleLarge,
//            textAlign = TextAlign.Center
//        )
//        SearchBar(query = query,
//            onQueryChange = onQueryChange,
//            onSearch = { },
//            active = true,
//            onActiveChange = { },
//            trailingIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.codescan),
//                    contentDescription = stringResource(
//                        id = R.string.codescan_description
//                    )
//                )
//            }
//        ) {
//            LazyColumn {
//                items(
//                items(
//                    count = items.size,
//                    key = null
//                ) { index ->
//                    items[index].run {
//                        LanguageItem(
//                            item = this,
//                            itemClickListener = itemClickListener,
//
//                        )
//                    }
//                }
//                    count = items.size,
//                    key = null
//                ) { index ->
//                    items[index].run {
//                        LanguageItem(
//                            item = this,
//                            itemClickListener = itemClickListener,
//
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//@Composable
//@Preview
//fun PreviewLanguageSearchBar() {
//    AppTheme {
//        LanguageSearchBar(
//            query = "Ko",
//            items = listOf(
//                SpecificLanguage("Kotlin", "kotlin"),
//                SpecificLanguage("Kotlin", "kotlin"),
//                SpecificLanguage("Kotlin", "kotlin"),
//                SpecificLanguage("Kotlin", "kotlin"),
//                SpecificLanguage("Kotlin", "kotlin"),
//            )
//        )
//    }
//}
//
//@Composable
//fun LanguageRadioItem(
//    repositoryLanguage: RepositoryLanguage
//) {
//
//}