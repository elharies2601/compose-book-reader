package id.elharies.composereader.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.elharies.composereader.model.BookRes
import id.elharies.composereader.model.UiState
import id.elharies.composereader.repository.book.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val bookRepo: BookRepository): ViewModel(), ISearchViewModel {

    private val _listBook: MutableStateFlow<UiState<BookRes>> = MutableStateFlow(UiState.Idle)
    override val listBook: StateFlow<UiState<BookRes>>
        get() = _listBook.asStateFlow()

    override fun searchBookByTitle(title: String) {
        _listBook.value = UiState.Loading
        viewModelScope.launch {
            try {
                _listBook.value = UiState.Success(bookRepo.searchBook(title))
            } catch (e: Exception) {
                _listBook.value = UiState.Failed(e.message ?: "Gagal Mencari Buku")
            }
        }
    }
}