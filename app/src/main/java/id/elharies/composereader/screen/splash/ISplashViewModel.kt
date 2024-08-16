package id.elharies.composereader.screen.splash

import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ISplashViewModel {
    val isLogin: StateFlow<UiState<Boolean>>
    fun checkIsLogin()
}