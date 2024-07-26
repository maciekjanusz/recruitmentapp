package dev.mjanusz.recruitmentapp.ui.trending

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mjanusz.recruitmentapp.AppState
import dev.mjanusz.recruitmentapp.R
import dev.mjanusz.recruitmentapp.data.remote.TrendingDateRange
import dev.mjanusz.recruitmentapp.ui.common.LoadingState
import dev.mjanusz.recruitmentapp.ui.common.TopBarAction
import dev.mjanusz.recruitmentapp.ui.common.UiModeHelper
import dev.mjanusz.recruitmentapp.ui.languages.LanguagePickerScreen
import dev.mjanusz.recruitmentapp.ui.model.AnyLanguage
import dev.mjanusz.recruitmentapp.ui.model.Repository
import dev.mjanusz.recruitmentapp.ui.model.RepositoryLanguage
import dev.mjanusz.recruitmentapp.ui.theme.AppShapes
import dev.mjanusz.recruitmentapp.ui.theme.AppTheme
import dev.mjanusz.recruitmentapp.ui.theme.AppTypography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    viewModel: TrendingViewModel = hiltViewModel()
) {
    val trendingRepositories by viewModel.trendingRepos.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedDateRange by viewModel.selectedDateRange.collectAsStateWithLifecycle()
    val loadingStates by viewModel.loadingStates.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val sheetState = rememberModalBottomSheetState()
    var showLanguagesSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        launch {
            viewModel.repositoryClicked.collect {
                appState.navController.navigate("repository/${it.owner}/${it.repo}") {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
        launch {
            viewModel.actionClicked.collect {
                showLanguagesSheet = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrendingAppBar(
                scrollBehavior = scrollBehavior,
                onSetUiMode = viewModel::onSetUiMode
            )
        }
    ) { innerPadding ->
        if (showLanguagesSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showLanguagesSheet = false },
            ) {
                LanguagePickerScreen(
                    appState = appState,
                    onLanguageSelected = {
                        showLanguagesSheet = false
                        viewModel.onLanguageSelected(it)
                    }
                )
            }
        }

        if (loadingStates == LoadingState.LOADING) {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            )
        }
        RepositoryListContent(
            modifier = Modifier.padding(innerPadding),
            selectedLanguage = selectedLanguage,
            selectedDateRange = selectedDateRange,
            repositories = trendingRepositories,
            itemClickListener = viewModel::onRepositoryClicked,
            favClickListener = viewModel::onRepositoryFavClicked,
            onActionClicked = viewModel::onActionClicked,
            onDateRangeSelected = viewModel::onDateRangeSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSetUiMode: (UiModeHelper.UiMode) -> Unit = { }
) {
    var showThemeDropdown by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.home_display_title),
                style = AppTypography.titleLarge,
            )
        },
        actions = {
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
            ) {
                IconButton(onClick = { showThemeDropdown = !showThemeDropdown }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(
                            id = R.string.codescan_description
                        )
                    )
                }
                DropdownMenu(expanded = showThemeDropdown,
                    onDismissRequest = { showThemeDropdown = false }) {
                    UiModeHelper.UiMode.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(it.stringRes)) },
                            onClick = {
                                showThemeDropdown = false
                                onSetUiMode(it)
                            }
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors().copy(
            scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewTrendingAppBar() {
    AppTheme(
        darkTheme = true
    ) {
        TrendingAppBar(scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior())
    }
}

@Composable
fun AssistChipsRow(
    selectedDateRange: TrendingDateRange,
    selectedLanguage: RepositoryLanguage = AnyLanguage,
    onActionClicked: (TopBarAction) -> Unit = { },
    onDateRangeSelected: (TrendingDateRange) -> Unit = { }
) {
    var dateRangeMenuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AssistChip(
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.languages_description)
                )
            },
            onClick = { onActionClicked(TopBarAction.OPEN_FILTER) },
            label = {
                Text(
                    text = if (selectedLanguage == AnyLanguage) {
                        stringResource(id = R.string.any_language)
                    } else {
                        selectedLanguage.name
                    },
                    style = AppTypography.labelSmall
                )
            },
            colors = AssistChipDefaults.assistChipColors().apply {
                if (selectedLanguage != AnyLanguage) {
                    copy(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
        ) {
            AssistChip(
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(id = R.string.languages_description)
                    )
                },
                onClick = { dateRangeMenuExpanded = !dateRangeMenuExpanded },
                label = {
                    Text(
                        text = selectedDateRange.displayString(),
                        style = AppTypography.labelSmall
                    )
                },
                colors = AssistChipDefaults.assistChipColors().apply {
                    if (selectedLanguage != AnyLanguage) {
                        copy(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = dateRangeMenuExpanded,
                onDismissRequest = { dateRangeMenuExpanded = false }) {

                TrendingDateRange.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.displayString(),
                                style = AppTypography.labelSmall
                            )
                        },
                        onClick = {
                            dateRangeMenuExpanded = false
                            onDateRangeSelected(it)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAssistChipsRow() {
    AppTheme {
        Surface {
            AssistChipsRow(selectedDateRange = TrendingDateRange.DAILY)
        }
    }
}

@Composable
fun RepositoryListContent(
    repositories: List<Repository>,
    selectedLanguage: RepositoryLanguage,
    selectedDateRange: TrendingDateRange,
    modifier: Modifier = Modifier,
    itemClickListener: (repository: Repository) -> Unit = { },
    favClickListener: (repository: Repository) -> Unit = { },
    onActionClicked: (TopBarAction) -> Unit = { },
    onDateRangeSelected: (TrendingDateRange) -> Unit = { }
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            AssistChipsRow(
                selectedLanguage = selectedLanguage,
                selectedDateRange = selectedDateRange,
                onActionClicked = onActionClicked,
                onDateRangeSelected = onDateRangeSelected
            )
        }
        items(
            count = repositories.size,
            key = null
        ) { index ->
            repositories[index].run {
                RepositoryItem(
                    item = this,
                    itemClickListener = itemClickListener,
                    favClickListener = favClickListener
                )
            }
        }
    }
}


@Composable
fun RepositoryItem(
    item: Repository,
    itemClickListener: (repository: Repository) -> Unit = { },
    favClickListener: (repository: Repository) -> Unit = { }
) {
    ListItem(
        modifier = Modifier.clickable {
            itemClickListener(item)
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .size(32.dp)
                    .clip(AppShapes.medium)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${item.rank + 1}",
                    style = AppTypography.labelMedium
                )
            }
        },
        trailingContent = {
            IconButton(
                onClick = { favClickListener(item) },
            ) {
                Icon(
                    painter = painterResource(
                        id = if (item.favourite) R.drawable.heart_fill else R.drawable.heart
                    ),
                    contentDescription = stringResource(id = R.string.heart_description)
                )
            }
        },
        headlineContent = {
            Row(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "${item.owner} / ",
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                    style = AppTypography.titleSmall
                )
                Text(
                    text = item.repo,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                    style = AppTypography.titleMedium
                )
            }
        },
        overlineContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${item.starsCount}",
                        textAlign = TextAlign.End,
                        style = AppTypography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.repo_forked),
                        contentDescription = stringResource(R.string.repo_forked_description)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${item.forksCount}",
                        textAlign = TextAlign.End,
                        style = AppTypography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = stringResource(R.string.star_description)
                    )
                }
                if (item.language != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = item.language,
                            textAlign = TextAlign.End,
                            style = AppTypography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                        if (item.languageColor != null) {
                            Image(
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.CenterVertically),
                                painter = painterResource(id = R.drawable.dot_fill),
                                colorFilter = ColorFilter.tint(item.languageColor),
                                contentDescription = stringResource(
                                    id = R.string.dot_fill_description
                                )
                            )
                        }
                    }
                }
            }
        },
        supportingContent = {
            Text(
                text = item.description.orEmpty(),
                overflow = TextOverflow.Clip,
                maxLines = 3,
                style = AppTypography.bodySmall
            )
        }
    )
}

@Composable
@Preview
fun PreviewUserRow() {
    RepositoryItem(
        item = Repository(
            rank = 1,
            owner = "maciekjanusz",
            repo = "recruitmentapp",
            url = "/maciekjanusz/recruitmentapp",
            description = "Application for recruitment purposes",
            language = "Kotlin",
            languageColor = Color.Blue,
            starsCount = 123,
            forksCount = 34,
            favourite = true
        )
    )
}