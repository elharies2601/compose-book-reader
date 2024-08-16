package id.elharies.composereader.screen.home

import com.google.firebase.auth.FirebaseUser
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.StateFlow

interface IHomeViewModel {
    val isSuccessLogout: StateFlow<UiState<Boolean>>
    val allBooks: StateFlow<UiState<MutableList<Book>>>
    val bookCollections: StateFlow<UiState<MutableList<Book>>>
    fun logOut()
    fun fetchAllBooks()
    fun fetchBooksByCollection()
}