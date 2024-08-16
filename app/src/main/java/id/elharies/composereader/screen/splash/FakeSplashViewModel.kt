package id.elharies.composereader.screen.splash

import androidx.lifecycle.ViewModel
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSplashViewModel: ViewModel(), ISplashViewModel {
    override val isLogin: StateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Idle)

    override fun checkIsLogin() {

    }
}