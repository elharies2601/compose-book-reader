package id.elharies.composereader.screen.details

import androidx.lifecycle.ViewModel
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.BookRes
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeDetailViewModel: ViewModel(), IDetailViewModel {
    override val detailBook: StateFlow<UiState<BookRes.Item>>
        get() = MutableStateFlow(UiState.Idle)
    override val saveBook: StateFlow<UiState<String>>
        get() = MutableStateFlow(UiState.Idle)

    override fun getDetailBook(idBook: String) {

    }

    override fun saveBookToDb(book: Book) {

    }
}