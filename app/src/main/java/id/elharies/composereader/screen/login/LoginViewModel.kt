package id.elharies.composereader.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import id.elharies.composereader.model.UiState
import id.elharies.composereader.repository.firebase.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: FirebaseRepository): ViewModel(), ILoginViewModel {

    private val _login: MutableStateFlow<UiState<AuthResult>> = MutableStateFlow(UiState.Idle)
    override val login: StateFlow<UiState<AuthResult>>
        get() = _login.asStateFlow()

    private val _signUp: MutableStateFlow<UiState<AuthResult>> = MutableStateFlow(UiState.Idle)
    override val signUp: StateFlow<UiState<AuthResult>>
        get() = _signUp.asStateFlow()

    override fun createUser(email: String, password: String) {
        _signUp.value = UiState.Loading
        viewModelScope.launch {
            repository.createUser(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val name = it.result.user?.email?.split("@")?.get(0) ?: "Unknown"
                    val uid = it.result.user?.uid ?: ""
                    saveUser(name, uid)

                    _signUp.value = UiState.Success(it.result)
                } else {
                    _signUp.value = UiState.Failed(it.exception?.message ?: "Gagal Membuat User")
                }
            }
        }
    }

    override fun login(email: String, password: String) {
        _login.value = UiState.Loading
        viewModelScope.launch {
            repository.loginUser(email, password).addOnSuccessListener {
                _login.value = UiState.Success(it)
            }.addOnFailureListener {
                _login.value = UiState.Failed(it.message ?: "gagal login")
            }
        }
    }

    private fun saveUser(name: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserIntoDb(name, uid)
        }
    }
}