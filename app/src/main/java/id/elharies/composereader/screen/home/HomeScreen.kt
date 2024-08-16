package id.elharies.composereader.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import id.elharies.composereader.component.BookCard
import id.elharies.composereader.component.EmptyScreen
import id.elharies.composereader.component.LoadingScreen
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import id.elharies.composereader.navigation.ReaderRoute
import id.elharies.composereader.utils.extension.navigateAndClean

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    vmHome: IHomeViewModel = FakeHomeViewModel()
) {
    val logOutState by vmHome.isSuccessLogout.collectAsStateWithLifecycle()
    val allBookState by vmHome.allBooks.collectAsStateWithLifecycle()
    val bookCollectState by vmHome.bookCollections.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    var allBooks by remember {
        mutableStateOf(listOf<Book>())
    }

    var bookCollected by remember {
        mutableStateOf(listOf<Book>())
    }

    LaunchedEffect(key1 = Unit) {
        vmHome.fetchAllBooks()
//        vmHome.fetchBooksByCollection()
    }

    LaunchedEffect(key1 = allBookState) {
        isLoading = allBookState is UiState.Loading
        when(allBookState) {
            is UiState.Failed -> {
                allBooks = mutableListOf()
                Toast.makeText(context, (allBookState as UiState.Failed).message, Toast.LENGTH_SHORT).show()
            }
            is UiState.Idle -> {}
            is UiState.Loading -> {}
            is UiState.Success -> {
                allBooks = (allBookState as UiState.Success).result
            }
        }
    }

    LaunchedEffect(key1 = logOutState) {
        when (logOutState) {
            is UiState.Failed -> {
                isLoading = false
            }

            is UiState.Idle -> {
                isLoading = false
            }

            is UiState.Loading -> {
                // show loading
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
                navController.navigateAndClean(ReaderRoute.Login.nameScreen)
            }
        }
    }

    LaunchedEffect(key1 = bookCollectState) {
        isLoading = bookCollectState is UiState.Loading

        if (bookCollectState is UiState.Failed) {
            Log.e("philo", (bookCollectState as UiState.Failed).message)
            Toast.makeText(context, (bookCollectState as UiState.Failed).message, Toast.LENGTH_SHORT).show()
            bookCollected = listOf()
            return@LaunchedEffect
        }

        if (bookCollectState is UiState.Success) {
            bookCollected = (bookCollectState as UiState.Success).result
        }
    }

    Scaffold(
        topBar = {
            ReaderTopBar(title = "Home") {
                vmHome.logOut()
            }
        },
        floatingActionButton = {
            FAB() {
                navController.navigate(ReaderRoute.Search.nameScreen)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            YourReadingScreen(
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                navController = navController,
                listBook = allBooks.filter { b ->
                    val currentUser = Firebase.auth.currentUser?.uid
                    b.startedReading != null && b.finishedReading == null && b.userId == currentUser
                }.toMutableList()
            )
            Spacer(modifier = Modifier.height(24.dp))
            ReadingListScreen(
                modifier = Modifier.fillMaxHeight(),
                navController = navController,
                listBook = allBooks.filter { b ->
                    b.startedReading == null && b.finishedReading == null
                }.toMutableList()
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
        if (isLoading) {
            LoadingScreen(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}

@Composable
private fun ReadingListScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    listBook: MutableList<Book> = mutableListOf()
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Reading List",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
        AnimatedVisibility(visible = listBook.isNotEmpty()) {
            LazyRow(modifier = Modifier.padding(top = 8.dp, start = 16.dp)) {
                items(listBook) {
                    BookCard(modifier = Modifier, book = it) { b ->
                        navController.navigate("${ReaderRoute.Update.nameScreen}/${b.bookId}")
                    }
                    Spacer(modifier = modifier.width(16.dp))
                }
            }
        }
        AnimatedVisibility(visible = listBook.isEmpty()) {
            EmptyScreen(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun YourReadingScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    listBook: MutableList<Book> = mutableListOf()
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Your reading activity right now",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
        AnimatedVisibility(visible = listBook.isNotEmpty()) {
            LazyRow(modifier = modifier.padding(top = 8.dp, start = 16.dp)) {
                items(listBook) {
                    BookCard(modifier = Modifier, book = it) { b ->
                        navController.navigate("${ReaderRoute.Update.nameScreen}/${b.bookId}")
                    }
                    Spacer(modifier = modifier.width(16.dp))
                }
            }
        }
        AnimatedVisibility(visible = listBook.isEmpty()) {
            EmptyScreen(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun FAB(onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a Book",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReaderTopBar(
    title: String,
    isShownProfile: Boolean = true,
    onClickLogout: () -> Unit = {}
) {
    TopAppBar(title = {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            if (isShownProfile) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(35.dp)
                        )
                        .scale(1f)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        // need call viewModel for logout
                        onClickLogout()
                    }
            )
        }
    })
}

@Preview(showBackground = true)
@Composable
private fun PreviewReadingListScreen() {
    val tempList = mutableListOf(
        Book(title = "Title 1", author = "Author 1", rating = 0.0),
        Book(title = "Title 1", author = "Author 1", rating = 0.0),
        Book(title = "Title 1", author = "Author 1", rating = 0.0),
        Book(title = "Title 1", author = "Author 1", rating = 0.0),
    )
    ReadingListScreen(listBook = tempList)
}

@Preview(showBackground = true)
@Composable
private fun PreviewYourReading() {
    val tempList = mutableListOf(
        Book(title = "Title 1", author = "Author 1", rating = 0.0),
        Book(title = "Title 1", author = "Author 1", rating = 0.0),
        Book(title = "Title 1", author = "Author 1", rating = 0.0),
        Book(title = "Title 1", author = "Author 1", rating = 0.0),
    )
    YourReadingScreen(listBook = tempList)
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}