package id.elharies.composereader.screen.login

import com.google.firebase.auth.AuthResult
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.StateFlow

interface ILoginViewModel {
    val login: StateFlow<UiState<AuthResult>>
    val signUp: StateFlow<UiState<AuthResult>>
    fun createUser(email: String, password: String)
    fun login(email: String, password: String)
}