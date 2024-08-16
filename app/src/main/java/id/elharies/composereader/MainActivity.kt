package id.elharies.composereader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import id.elharies.composereader.navigation.ReaderNavigation
import id.elharies.composereader.screen.details.DetailViewModel
import id.elharies.composereader.screen.home.HomeViewModel
import id.elharies.composereader.screen.login.LoginViewModel
import id.elharies.composereader.screen.search.SearchViewModel
import id.elharies.composereader.screen.splash.SplashViewModel
import id.elharies.composereader.ui.theme.ComposeReaderTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private val splashViewModel by viewModels<SplashViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeReaderTheme {
                // A surface container using the 'background' color from the theme
                ComposeReaderApp()
            }
        }
    }

    @Composable
    private fun ComposeReaderApp() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                ReaderNavigation(
                    loginVm = loginViewModel,
                    splashVm = splashViewModel,
                    homeVm = homeViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeReaderTheme {
        Greeting("Android")
    }
}