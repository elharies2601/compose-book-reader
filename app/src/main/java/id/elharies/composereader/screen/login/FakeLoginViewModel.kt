package id.elharies.composereader.screen.login

import com.google.firebase.auth.AuthResult
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeLoginViewModel: ILoginViewModel {
    override val login: StateFlow<UiState<AuthResult>>
        get() = MutableStateFlow(UiState.Idle)
    override val signUp: StateFlow<UiState<AuthResult>>
        get() = MutableStateFlow(UiState.Idle)

    override fun createUser(email: String, password: String) {

    }

    override fun login(email: String, password: String) {

    }
}