package id.elharies.composereader.screen.splash

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import id.elharies.composereader.model.UiState
import id.elharies.composereader.navigation.ReaderRoute
import id.elharies.composereader.utils.extension.navigateAndClean
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ISplashViewModel = FakeSplashViewModel()
) {
    val scale = remember {
        Animatable(0f)
    }
    val isLoginState by viewModel.isLogin.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = isLoginState) {
        if (isLoginState is UiState.Success) {
            val isLogin = (isLoginState as UiState.Success).result
            if (isLogin) {
                navController.navigateAndClean(ReaderRoute.Home.nameScreen)
            } else {
                navController.navigateAndClean(ReaderRoute.Login.nameScreen)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f, animationSpec = tween(durationMillis = 800, easing = {
            OvershootInterpolator(8f).getInterpolation(it)
        }))
        delay(2000L)
        viewModel.checkIsLogin()
    }

    Surface(
        modifier = modifier
            .size(330.dp)
            .padding(15.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(1.dp)
        ) {
            Text(
                text = "Reader",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Red.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Read. Change. Yourself",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.LightGray
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSplashScreen() {
    SplashScreen(navController = rememberNavController())
}