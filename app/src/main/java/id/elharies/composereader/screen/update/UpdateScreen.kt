package id.elharies.composereader.screen.update

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.request.ImageRequest
import com.google.firebase.Timestamp
import com.google.gson.Gson
import id.elharies.composereader.component.LoadingScreen
import id.elharies.composereader.component.RatingBar
import id.elharies.composereader.component.TopBarWithBackIcon
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import id.elharies.composereader.utils.constans.ConstStatusReading
import id.elharies.composereader.utils.extension.formatDate
import kotlinx.coroutines.delay
import retrofit2.http.Header

@Composable
fun UpdateScreen(
    navController: NavController = rememberNavController(),
    bookId: String = "",
    viewModel: IUpdateScreenViewModel = FakeUpdateScreenViewModel()
) {
    val bookState by viewModel.books.collectAsStateWithLifecycle()
    val deletedState by viewModel.deletedBook.collectAsStateWithLifecycle()
    val updatedState by viewModel.updatedBook.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var isLoading by remember {
        mutableStateOf(false)
    }

    var book by rememberSaveable {
        mutableStateOf(Book())
    }

    var notesValue by rememberSaveable {
        mutableStateOf("")
    }

    var ratingValue by rememberSaveable {
        mutableDoubleStateOf(0.0)
    }

    var isShowDialogDelete by rememberSaveable {
        mutableStateOf(false)
    }

    var isStartReading by rememberSaveable {
        mutableStateOf(false)
    }

    var isFinishReading by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchItemBook(bookId)
    }

    LaunchedEffect(key1 = bookState) {
        isLoading = bookState is UiState.Loading

        if (bookState is UiState.Failed) {
            Log.e("UpdateScreen", (bookState as UiState.Failed).message)
            return@LaunchedEffect
        }

        if (bookState is UiState.Success) {
            val (bookOne) = (bookState as UiState.Success).result
            book = bookOne
            notesValue = book.notes ?: ""
            ratingValue = book.rating
            isStartReading = book.startedReading != null
            isFinishReading = book.finishedReading != null
        }
    }

    LaunchedEffect(key1 = deletedState) {
        isLoading = deletedState is UiState.Loading

        if (deletedState is UiState.Failed) {
            Log.e("UpdateScreenDel", (deletedState as UiState.Failed).message)
            showToast(context, (deletedState as UiState.Failed).message)
            return@LaunchedEffect
        }

        if (deletedState is UiState.Success) {
            isShowDialogDelete = false
            showToast(context, "Berhasil Hapus")
            delay(1000L)
            navController.popBackStack()
        }
    }

    LaunchedEffect(key1 = updatedState) {
        isLoading = updatedState is UiState.Loading

        if (updatedState is UiState.Failed) {
            Log.e("UpdateScreenDel", (updatedState as UiState.Failed).message)
            showToast(context, (updatedState as UiState.Failed).message)
            return@LaunchedEffect
        }

        if (updatedState is UiState.Success) {
            showToast(context, (updatedState as UiState.Success).result)
            delay(1000L)
            navController.popBackStack()
        }
    }

    Scaffold(topBar = {
        TopBarWithBackIcon(title = "Update Book") {
            navController.popBackStack()
        }
    }) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Header(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                book = book
            )
            Spacer(modifier = Modifier.height(16.dp))
            NotesField(
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = book.finishedReading != null,
                value = notesValue
            ) { value ->
                notesValue = value
            }
            Spacer(modifier = Modifier.height(16.dp))
            ReadingButtons(
                modifier = Modifier.padding(horizontal = 16.dp),
                book = book,
                isStarted = isStartReading,
                isFinish = isFinishReading,
                onClickStart = {
                    isStartReading = true
                },
                onClickMarkRead = {
                    isFinishReading = true
                })
            Spacer(modifier = Modifier.height(24.dp))
            RatingScreen(
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = book.finishedReading != null,
                rating = ratingValue
            ) { rate ->
                ratingValue = rate
            }
            Spacer(modifier = Modifier.height(36.dp))
            AnimatedVisibility(visible = book.finishedReading == null) {
                ListButton(modifier = Modifier.padding(horizontal = 16.dp), onClickSave = {
                    updateBook(isStartReading, book, isFinishReading, notesValue, ratingValue, viewModel)
                }, onClickDelete = {
                    isShowDialogDelete = true
                })
            }
            Spacer(modifier = Modifier.height(16.dp))
            AlertDialogDelete(
                isOpen = isShowDialogDelete,
                onDismiss = { isShowDialogDelete = false },
                onYes = {
                    viewModel.deleteItemBook(book.id ?: "")
                })
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

private fun updateBook(
    isStartReading: Boolean,
    book: Book,
    isFinishReading: Boolean,
    notesValue: String,
    ratingValue: Double,
    viewModel: IUpdateScreenViewModel
) {
    val startTimeStamp = if (isStartReading) Timestamp.now() else book.startedReading
    val finishTimeStamp = if (isFinishReading) Timestamp.now() else book.finishedReading
    val statusReading = when {
        isStartReading -> ConstStatusReading.reading
        isFinishReading -> ConstStatusReading.finished
        else -> book.statusReading
    }
    val newBook = book.copy(
        notes = notesValue,
        rating = ratingValue,
        startedReading = startTimeStamp,
        finishedReading = finishTimeStamp,
        statusReading = statusReading
    )
    viewModel.updateItemBook(newBook, newBook.id ?: "")
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    book: Book = Book()
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val painter =
                ImageRequest.Builder(LocalContext.current).crossfade(true).data(book.linkImage)
                    .build()
            AsyncImage(
                model = painter, contentDescription = "Image Card", modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = book.date,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NotesField(
    modifier: Modifier = Modifier,
    value: String = "",
    enabled: Boolean = true,
    onNotesChange: (String) -> Unit = {}
) {
    val currentKeyboard = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onNotesChange,
        label = {
            Text(text = "Enter your thoughts")
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            currentKeyboard?.hide()
        }),
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
    )
}

@Composable
private fun ReadingButtons(
    modifier: Modifier = Modifier,
    book: Book = Book(),
    isStarted: Boolean = false,
    isFinish: Boolean = false,
    onClickStart: () -> Unit = {},
    onClickMarkRead: () -> Unit = {}
) {
    val textStart = if (book.startedReading == null) {
        if (!isStarted) {
            "Start Reading"
        } else {
            "Started Reading!"
        }
    } else {
        "Started on ${book.startedReading?.formatDate()}"
    }

    val textFinished = if (book.finishedReading == null) {
        if (!isFinish) {
            "Mark as Read"
        } else {
            "Finished Reading!"
        }
    } else {
        "Finished on ${book.startedReading?.formatDate()}"
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = textStart,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (book.statusReading != ConstStatusReading.finished || book.startedReading == null) {
                        onClickStart()
                    }
                }
        )
        Text(
            text = textFinished,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
            color = MaterialTheme.colorScheme.onErrorContainer,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (book.statusReading != ConstStatusReading.finished || book.finishedReading == null) {
                        onClickMarkRead()
                    }
                }
        )
    }
}

@Composable
private fun RatingScreen(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    enabled: Boolean = true,
    onChangeRating: (Double) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Rating",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        RatingBar(modifier = Modifier.padding(top = 8.dp), rating = rating, enabled = enabled) {
            onChangeRating(it)
        }
    }
}

@Composable
private fun ListButton(
    modifier: Modifier = Modifier,
    onClickSave: () -> Unit = {},
    onClickDelete: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onClickSave, modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .padding(end = 4.dp)
        ) {
            Text(text = "Update")
        }
        Button(
            onClick = onClickDelete,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .padding(start = 4.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)
        ) {
            Text(text = "Delete", color = MaterialTheme.colorScheme.onErrorContainer)
        }
    }
}

@Composable
private fun AlertDialogDelete(
    isOpen: Boolean = false,
    onDismiss: () -> Unit = {},
    onYes: () -> Unit = {}
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Text(text = "Delete!", modifier = Modifier.clickable { onYes() })
            },
            dismissButton = {
                Text(text = "Cancel", modifier = Modifier.clickable { onDismiss() })
            },
            text = {
                Text(text = "Are you sure for deleting this book ?")
            },
            title = {
                Text(text = "Delete Book?", style = MaterialTheme.typography.titleLarge)
            },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            textContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

private fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

@Preview
@Composable
private fun PreviewListButton() {
    ListButton()
}

@Preview(showBackground = true)
@Composable
private fun PreviewRatingScreen() {
    RatingScreen()
}

@Preview(showBackground = true)
@Composable
private fun PreviewHeader() {
    val book = Book(title = "Book 1", author = "Author 1 Author 1", date = "2023-02-02")
    Header(book = book)
}

@Preview
@Composable
private fun PreviewUpdateScreen() {
    UpdateScreen()
}