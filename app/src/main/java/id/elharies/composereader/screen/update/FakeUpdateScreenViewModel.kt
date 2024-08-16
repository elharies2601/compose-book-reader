package id.elharies.composereader.screen.update

import androidx.lifecycle.ViewModel
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeUpdateScreenViewModel: ViewModel(), IUpdateScreenViewModel {
    override val books: StateFlow<UiState<List<Book>>>
        get() = MutableStateFlow(UiState.Idle)
    override val deletedBook: StateFlow<UiState<String>>
        get() = MutableStateFlow(UiState.Idle)
    override val updatedBook: StateFlow<UiState<String>>
        get() = MutableStateFlow(UiState.Idle)

    override fun fetchItemBook(bookId: String) {

    }

    override fun updateItemBook(book: Book, id: String) {

    }

    override fun deleteItemBook(documentId: String) {

    }
}