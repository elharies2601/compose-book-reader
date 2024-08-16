package id.elharies.composereader.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.elharies.composereader.model.UiState
import id.elharies.composereader.repository.firebase.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: FirebaseRepository) : ViewModel(),
    ISplashViewModel {

    private val _isLogin: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Idle)
    override val isLogin: StateFlow<UiState<Boolean>>
        get() = _isLogin.asStateFlow()

    override fun checkIsLogin() {
        _isLogin.value = UiState.Loading
        viewModelScope.launch {
            _isLogin.value = UiState.Success(repository.isLogin())
        }
    }
}