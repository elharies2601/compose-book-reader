package id.elharies.composereader.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.BookRes
import id.elharies.composereader.model.UiState
import id.elharies.composereader.repository.book.BookRepository
import id.elharies.composereader.repository.firebase.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: BookRepository, private val fireRepo: FirebaseRepository) : ViewModel(),
    IDetailViewModel {

    private val _detailBook: MutableStateFlow<UiState<BookRes.Item>> = MutableStateFlow(UiState.Idle)
    override val detailBook: StateFlow<UiState<BookRes.Item>>
        get() = _detailBook.asStateFlow()

    private val _saveBook: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Idle)
    override val saveBook: StateFlow<UiState<String>>
        get() = _saveBook.asStateFlow()

    override fun getDetailBook(idBook: String) {
        _detailBook.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.fetchDetailBook(idBook)
            try {
                _detailBook.value = UiState.Success(result)
            } catch (e: HttpException) {
                result.error?.let {
                    _detailBook.value = UiState.Failed(it.message)
                } ?: run {
                    _detailBook.value = UiState.Failed("Tidak Bisa Mendapatkan detail buku")
                }
            }
        }
    }

    override fun saveBookToDb(book: Book) {
        _saveBook.value = UiState.Loading
        viewModelScope.launch {
            fireRepo.saveBookIntoDb(book).addOnCompleteListener {
                if (it.isSuccessful) {
                    val docId = it.result.id
                    fireRepo.updateBook(mapOf("id" to docId), docId).addOnCompleteListener { u ->
                        if (u.isSuccessful) {
                            _saveBook.value = UiState.Success("Berhasil Menyimpan Buku")
                        } else {
                            _saveBook.value = UiState.Failed(u.exception?.message ?: "")
                        }
                    }
                } else {
                    _saveBook.value = UiState.Failed(it.exception?.message ?: "")
                }
            }
        }
    }
}