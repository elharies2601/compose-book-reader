package id.elharies.composereader.screen.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import id.elharies.composereader.repository.firebase.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(private val repository: FirebaseRepository): ViewModel(), IUpdateScreenViewModel {

    private val _books: MutableStateFlow<UiState<List<Book>>> = MutableStateFlow(UiState.Idle)
    override val books: StateFlow<UiState<List<Book>>>
        get() = _books.asStateFlow()

    private val _deletedBook: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Idle)
    override val deletedBook: StateFlow<UiState<String>>
        get() = _deletedBook.asStateFlow()

    private val _updatedBook: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Idle)
    override val updatedBook: StateFlow<UiState<String>>
        get() = _updatedBook.asStateFlow()

    override fun fetchItemBook(bookId: String) {
        _books.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repository.getBook(bookId).map {
                    it.toObject(Book::class.java) ?: Book()
                }
                if (result.isEmpty()) {
                    _books.value = UiState.Success(mutableListOf())
                } else {
                    _books.value = UiState.Success(result)
                }
            } catch (e: Exception) {
                _books.value = UiState.Failed(e.message ?: "")
            }
        }
    }

    override fun updateItemBook(book: Book, id: String) {
        _updatedBook.value = UiState.Loading
        viewModelScope.launch {
            try {
                val payload = mapOf(
                    "notes" to book.notes,
                    "rating" to book.rating,
                    "startedReading" to book.startedReading,
                    "finishedReading" to book.finishedReading,
                    "statusReading" to book.statusReading
                )
                repository.updateBook(payload, id).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _updatedBook.value = UiState.Success("Behasil Update")
                    } else {
                        _updatedBook.value = UiState.Failed(it.exception?.message ?: "Gagal Update")
                    }
                }
            } catch (e: Exception) {
                _updatedBook.value = UiState.Failed(e.message ?: "")
            }
        }
    }

    override fun deleteItemBook(documentId: String) {
        _deletedBook.value = UiState.Loading
        viewModelScope.launch {
            try {
                repository.deleteBook(documentId).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _deletedBook.value = UiState.Success("Sukses Menghapus")
                    } else {
                        _deletedBook.value = UiState.Failed(it.exception?.message ?: "")
                    }
                }
            } catch (e: Exception) {
                _deletedBook.value = UiState.Failed(e.message ?: "")
            }
        }
    }
}