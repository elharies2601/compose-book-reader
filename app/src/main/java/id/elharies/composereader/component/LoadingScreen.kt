package id.elharies.composereader.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(70.dp),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeWidth = 6.dp
    )
}

@Preview(name = "LoadingScreen", showBackground = true)
@Composable
private fun PreviewLoadingScreen() {
    LoadingScreen()
}