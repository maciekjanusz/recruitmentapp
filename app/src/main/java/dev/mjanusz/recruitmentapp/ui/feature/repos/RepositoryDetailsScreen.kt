package dev.mjanusz.recruitmentapp.ui.feature.repos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.mjanusz.recruitmentapp.R
import dev.mjanusz.recruitmentapp.ui.model.RepositoryDetails
import dev.mjanusz.recruitmentapp.ui.model.RepositoryOwner
import dev.mjanusz.recruitmentapp.ui.theme.AppTheme
import dev.mjanusz.recruitmentapp.ui.theme.AppTypography
import dev.mjanusz.recruitmentapp.ui.common.LoadingState
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailsScreen(
    owner: String,
    repo: String,
    viewModel: RepositoryDetailsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val loadingStates by viewModel.loadingStates.collectAsStateWithLifecycle()
    val repositoryDetails by viewModel.repositoryDetails.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "$owner / $repo", style =
                        AppTypography.titleLarge
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
            )
        }
    ) { innerPadding ->
        if (loadingStates == LoadingState.LOADING) {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            )
        }

        RepositoryDetailsContent(
            modifier = Modifier.padding(innerPadding),
            repositoryDetails = repositoryDetails
        )
    }
}

@Composable
fun RepositoryDetailsContent(
    repositoryDetails: RepositoryDetails?,
    modifier: Modifier = Modifier
) {
    if (repositoryDetails != null) {
        Box(modifier = modifier) {
            Column(modifier = Modifier.padding(16.dp)) {
                DescriptionSection(repositoryDetails)
                OwnerSection(repositoryDetails)
                LanguageSection(repositoryDetails)
                StatsSection(repositoryDetails)
            }
        }
    }
}

@Composable
private fun DescriptionSection(repositoryDetails: RepositoryDetails) {
    if (repositoryDetails.description != null) {
        Text(
            text = stringResource(id = R.string.description),
            style = AppTypography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            elevation = CardDefaults.elevatedCardElevation(),
            onClick = { }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = repositoryDetails.description,
                    style = AppTypography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LanguageSection(repositoryDetails: RepositoryDetails) {
    Text(
        text = stringResource(id = R.string.language),
        style = AppTypography.titleMedium
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        onClick = { }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = repositoryDetails.language,
                style = AppTypography.titleLarge
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun OwnerSection(repositoryDetails: RepositoryDetails) {
    Text(
        text = stringResource(id = R.string.owner),
        style = AppTypography.titleMedium
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        onClick = { }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = repositoryDetails.owner.avatarUrl,
                contentDescription = stringResource(
                    id = R.string.content_desc_user_avatar, repositoryDetails.owner.login
                ),
                placeholder = painterResource(R.drawable.drawable_gh_user_avatar_placeholder),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(32.dp)
            )
            Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                text = repositoryDetails.owner.login,
                style = AppTypography.titleLarge
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun StatsSection(repositoryDetails: RepositoryDetails) {
    Text(
        text = stringResource(id = R.string.stats),
        style = AppTypography.titleMedium
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        onClick = { }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = stringResource(id = R.string.star_description)
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "${repositoryDetails.stargazersCount}",
                    style = AppTypography.headlineSmall
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.repo_forked),
                    contentDescription = stringResource(id = R.string.repo_forked_description)
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "${repositoryDetails.forksCount}",
                    style = AppTypography.headlineSmall
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.eye),
                    contentDescription = stringResource(id = R.string.eye_description)
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "${repositoryDetails.watchersCount}",
                    style = AppTypography.headlineSmall
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
@Preview
fun PreviewRepositoryDetailsContent() {
    AppTheme {
        Surface {
            RepositoryDetailsContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                repositoryDetails = RepositoryDetails(
                    id = 764707505,
                    name = "recruitmentapp",
                    fullName = "maciekjanusz/recruitmentapp",
                    private = false,
                    htmlUrl = "https://github.com/maciekjanusz/recruitmentapp",
                    description = "Lorem ipsum dolor sit amet",
                    owner = RepositoryOwner(
                        id = 8041839,
                        login = "maciekjanusz",
                        avatarUrl = "https://avatars.githubusercontent.com/u/8041839?v=4",
                        htmlUrl = "https://github.com/maciekjanusz",
                        type = "User"
                    ),
                    createdAt = OffsetDateTime.now(),
                    updatedAt = OffsetDateTime.now(),
                    pushedAt = OffsetDateTime.now(),
                    stargazersCount = 123,
                    watchersCount = 12,
                    forksCount = 3,
                    language = "Kotlin",
                )
            )
        }
    }
}
