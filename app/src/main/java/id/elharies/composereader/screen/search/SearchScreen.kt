package id.elharies.composereader.screen.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import id.elharies.composereader.component.SearchBookCard
import id.elharies.composereader.component.TopBarWithBackIcon
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import id.elharies.composereader.navigation.ReaderRoute
import id.elharies.composereader.utils.mapper.toMap
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun SearchScreen(
    navController: NavController = rememberNavController(),
    searchVm: ISearchViewModel = FakeSearchViewModel()
) {
    var bookValue by rememberSaveable {
        mutableStateOf("")
    }

    var isLoadingSearch by rememberSaveable {
        mutableStateOf(false)
    }

    val listBookState by searchVm.listBook.debounce(500L).collectAsStateWithLifecycle(UiState.Idle)

    LaunchedEffect(key1 = listBookState) {
        isLoadingSearch = listBookState is UiState.Loading
    }

    Scaffold(topBar = {
        TopBarWithBackIcon(title = "Search") {
            navController.popBackStack()
        }
    }) {
        Surface(modifier = Modifier.padding(it)) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    isLoading = isLoadingSearch,
                    valueSearch = bookValue
                ) { value ->
                    bookValue = value
                    if (value.length > 3) {
                        searchVm.searchBookByTitle(value)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (listBookState is UiState.Success) {
                    val itemCount = (listBookState as UiState.Success).result.totalItems
                    AnimatedVisibility(visible = itemCount > 0) {
                        val list = (listBookState as UiState.Success).result.items
                        LazyColumn(modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .height(500.dp)) {
                            items(list) {b ->
                                SearchBookCard(book = b.toMap(), modifier = Modifier) {
                                    // navigate to detail
                                    navController.navigate("${ReaderRoute.Detail.nameScreen}/${b.id}")
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }

                    AnimatedVisibility(visible = itemCount == 0) {
                        NoDataScreen(Modifier)
                    }
                }
            }
        }
    }
}

@Composable
private fun NoDataScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Buku Tidak Ditemukan",
        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .wrapContentHeight(),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    valueSearch: String = "",
    onSearch: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = valueSearch,
        onValueChange = onSearch,
        maxLines = 2,
        label = { Text(text = "Search") },
        trailingIcon = {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(25.dp))
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
        }),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNoDataScreen() {
    NoDataScreen()
}

@Preview(showBackground = true)
@Composable
private fun PreviewSearchField() {
    SearchField()
}

@Preview
@Composable
private fun PreviewSearchScreen() {
    SearchScreen()
}