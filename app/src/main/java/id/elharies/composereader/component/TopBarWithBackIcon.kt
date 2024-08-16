package id.elharies.composereader.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBackIcon(
    modifier: Modifier = Modifier,
    title: String = "",
    onClickBack: () -> Unit = {}
) {
    TopAppBar(title = {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
    }, navigationIcon = {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "Back button",
            modifier = Modifier.clickable { onClickBack() }.padding(start = 8.dp))
    }, modifier = modifier)
}

@Preview(name = "TopBarWithBackIcon")
@Composable
private fun PreviewTopBarWithBackIcon() {
    TopBarWithBackIcon(title = "Search")
}