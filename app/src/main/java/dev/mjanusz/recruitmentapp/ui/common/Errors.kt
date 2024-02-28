package dev.mjanusz.recruitmentapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mjanusz.recruitmentapp.R
import dev.mjanusz.recruitmentapp.ui.theme.AppTypography

@Composable
fun ErrorIndicatorWithRetry(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .wrapContentSize()
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.drawable_gh_user_avatar_placeholder),
                contentDescription = stringResource(id = R.string.cd_error),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = message,
                style = AppTypography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedButton(onClick = onRetry) {
                Text(text = stringResource(id = R.string.action_retry))
            }
        }
    }
}

@Preview
@Composable
fun PreviewErrorIndicatorWithRetry() {
    ErrorIndicatorWithRetry(message = "Something went wrong.")
}