package id.elharies.composereader.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import id.elharies.composereader.repository.firebase.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirebaseRepository) : ViewModel(),
    IHomeViewModel {
    private val _isSuccessLogout: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Idle)
    override val isSuccessLogout: StateFlow<UiState<Boolean>>
        get() = _isSuccessLogout.asStateFlow()

    private val _allBooks: MutableStateFlow<UiState<MutableList<Book>>> =
        MutableStateFlow(UiState.Idle)
    override val allBooks: StateFlow<UiState<MutableList<Book>>>
        get() = _allBooks.asStateFlow()

    private val _bookCollect: MutableStateFlow<UiState<MutableList<Book>>> =
        MutableStateFlow(UiState.Idle)
    override val bookCollections: StateFlow<UiState<MutableList<Book>>>
        get() = _bookCollect.asStateFlow()

    override fun logOut() {
        _isSuccessLogout.value = UiState.Loading
        repository.signOutUser()
        viewModelScope.launch {
            _isSuccessLogout.value = UiState.Success(repository.isLogin())
        }
    }

    override fun fetchAllBooks() {
        _allBooks.value = UiState.Loading
        viewModelScope.launch {
            try {
                val temp = repository.getAllBooks().get().await().documents.map {
                    it.toObject(Book::class.java)!!
                }
                if (temp.isEmpty()) {
                    _allBooks.value = UiState.Success(mutableListOf())
                } else {
                    _allBooks.value = UiState.Success(temp.toMutableList())
                }
            } catch (e: FirebaseFirestoreException) {
                _allBooks.value = UiState.Failed(e.message ?: "")
            }
        }
    }

    override fun fetchBooksByCollection() {
        _bookCollect.value = UiState.Loading
        viewModelScope.launch {
            try {
                val temp = repository.getBooksByUser().whereNotEqualTo("startedReading", null)
                    .whereEqualTo("finishedReading", null)
                    .get().await().documents.map {
                        it.toObject(Book::class.java) ?: Book()
                    }
                if (temp.isEmpty()) {
                    _bookCollect.value = UiState.Success(mutableListOf())
                } else {
                    _bookCollect.value = UiState.Success(temp.toMutableList())
                }
            } catch (e: FirebaseFirestoreException) {
                _bookCollect.value = UiState.Failed(e.message ?: "")
            }
        }
    }
}