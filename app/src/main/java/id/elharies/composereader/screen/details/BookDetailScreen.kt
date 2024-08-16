package id.elharies.composereader.screen.details

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import id.elharies.composereader.component.LoadingScreen
import id.elharies.composereader.component.RoundedImage
import id.elharies.composereader.component.TopBarWithBackIcon
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import id.elharies.composereader.utils.extension.toTextFromHtml
import id.elharies.composereader.utils.mapper.toMap
import kotlinx.coroutines.launch

@Composable
fun BookDetailScreen(
    navController: NavController = rememberNavController(),
    vmDetail: IDetailViewModel = FakeDetailViewModel(),
    bookId: String = ""
) {
    val detailState by vmDetail.detailBook.collectAsState()
    val saveState by vmDetail.saveBook.collectAsState()

    var book by rememberSaveable {
        mutableStateOf(Book())
    }
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        vmDetail.getDetailBook(bookId)
    }

    LaunchedEffect(key1 = detailState) {
        isLoading = detailState is UiState.Loading
        when (detailState) {
            is UiState.Failed -> {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        (detailState as UiState.Failed).message,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is UiState.Idle -> {}
            is UiState.Loading -> {}
            is UiState.Success -> {
                book = (detailState as UiState.Success).result.toMap()
            }
        }
    }

    LaunchedEffect(key1 = saveState) {
        Log.e("philo", "$saveState")
        isLoading = saveState is UiState.Loading
        when(saveState) {
            is UiState.Failed -> {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        (saveState as UiState.Failed).message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is UiState.Idle -> {}
            is UiState.Loading -> {}
            is UiState.Success -> {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        (saveState as UiState.Success).result,
                        duration = SnackbarDuration.Short
                    )
                }

                navController.popBackStack()
            }
        }
    }

    Scaffold(topBar = {
        TopBarWithBackIcon(title = "Detail Book") {
            navController.popBackStack()
        }
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) {
        Surface(modifier = Modifier.padding(it), color = MaterialTheme.colorScheme.background) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    item {
                        RoundedImage(
                            modifier = Modifier.padding(top = 24.dp),
                            linkImage = book.linkImage,
                            size = 200.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Text(
                            text = book.author,
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Text(
                            text = book.date,
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Normal),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        Text(
                            text = book.desc.toTextFromHtml(),
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Normal,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                }
                TwoButtonScreen(navController) {
                    vmDetail.saveBookToDb(book)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        if (isLoading) {
            LoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
private fun TwoButtonScreen(navController: NavController, onSaveBook: () -> Unit = {}) {
    Button(
        onClick = {
            onSaveBook()
        },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp),
        shape = CircleShape
    ) {
        Text(text = "Save", color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = {
            navController.popBackStack()
        },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp),
        shape = CircleShape
    ) {
        Text(text = "Cancel", color = MaterialTheme.colorScheme.onError)
    }
}

@Preview
@Composable
private fun PreviewBookDetailScreen() {
    BookDetailScreen()
}