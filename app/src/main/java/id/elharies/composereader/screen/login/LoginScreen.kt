package id.elharies.composereader.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import id.elharies.composereader.model.UiState
import id.elharies.composereader.navigation.ReaderRoute
import id.elharies.composereader.utils.extension.navigateAndClean
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: ILoginViewModel = FakeLoginViewModel()
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()

    val loginState by loginViewModel.login.collectAsState()
    val signUpState by loginViewModel.signUp.collectAsState()

    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    var isCreateAccount by rememberSaveable {
        mutableStateOf(false)
    }

    // handle login
    LaunchedEffect(key1 = loginState) {
        when (loginState) {
            is UiState.Idle -> {}
            is UiState.Loading -> isLoading = true
            is UiState.Failed -> {
                isLoading = false
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        (loginState as UiState.Failed).message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is UiState.Success -> {
                // handle clear back stacks and save current user
                isLoading = false
                navController.navigateAndClean(ReaderRoute.Home.nameScreen)
            }
        }
    }

    // handle sign up
    LaunchedEffect(key1 = signUpState) {
        when (signUpState) {
            is UiState.Failed -> {
                isLoading = false
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        (signUpState as UiState.Failed).message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is UiState.Idle -> {}
            is UiState.Loading -> {
                isLoading = true
            }
            is UiState.Success -> {
                isLoading = false
                navController.navigateAndClean(ReaderRoute.Home.nameScreen)
            }
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                text = "Reader",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Red.copy(alpha = 0.5f),
                modifier = modifier.padding(top = 16.dp)
            )
            Spacer(modifier = modifier.height(32.dp))
            AnimatedVisibility(visible = isCreateAccount) {
                Text(
                    text = "Please enter a valid email and password that is at least 6 characters",
                    modifier = modifier.padding(horizontal = 16.dp)
                )
            }
            AnimatedVisibility(visible = isCreateAccount) {
                Spacer(modifier = modifier.height(16.dp))
            }
            UserForm(
                modifier,
                isLoading = isLoading,
                isCreateAccount = isCreateAccount
            ) { email, password ->
                if (isCreateAccount) {
                    loginViewModel.createUser(email, password)
                } else {
                    loginViewModel.login(email, password)
                }
            }
            Spacer(modifier = modifier.height(36.dp))
            Text(
                text = buildAnnotatedString {
                    if (isCreateAccount) {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                            append("Have Account?")
                        }
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("  Login")
                        }
                    } else {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                            append("New User?")
                        }
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("  Sign Up")
                        }
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { isCreateAccount = !isCreateAccount })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserForm(
    modifier: Modifier,
    isLoading: Boolean = true,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { _, _ -> }
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isValid = remember(email, password) {
        email.trim().isNotEmpty() && (password.trim().isNotEmpty() && password.trim().length >= 6)
    }
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = {
            Text(text = "E-mail")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
    Spacer(modifier = modifier.height(16.dp))
    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Password") },
        enabled = !isLoading,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            if (passwordVisibility) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Visible Password",
                    modifier = Modifier.clickable {
                        passwordVisibility = false
                    }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.VisibilityOff,
                    contentDescription = "Invisible Password",
                    modifier = Modifier.clickable {
                        passwordVisibility = true
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
    Spacer(modifier = modifier.height(32.dp))
    Button(
        onClick = {
            onDone(email.trim(), password.trim())
            keyboardController?.hide()
        },
        enabled = isValid && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = modifier.size(25.dp))
        } else {
            Text(
                text = if (isCreateAccount) "Create Account" else "Login",
                fontSize = TextUnit(16f, TextUnitType.Sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}