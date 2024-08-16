package id.elharies.composereader.screen.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeHomeViewModel: ViewModel(), IHomeViewModel {
    override val isSuccessLogout: StateFlow<UiState<Boolean>>
        get() = MutableStateFlow(UiState.Idle)
    override val allBooks: StateFlow<UiState<MutableList<Book>>>
        get() = MutableStateFlow(UiState.Idle)
    override val bookCollections: StateFlow<UiState<MutableList<Book>>>
        get() = MutableStateFlow(UiState.Idle)

    override fun logOut() {

    }

    override fun fetchAllBooks() {

    }

    override fun fetchBooksByCollection() {

    }
}