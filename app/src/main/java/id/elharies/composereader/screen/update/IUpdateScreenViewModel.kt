package id.elharies.composereader.screen.update

import id.elharies.composereader.model.Book
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IUpdateScreenViewModel {
    val books: StateFlow<UiState<List<Book>>>
    val deletedBook: StateFlow<UiState<String>>
    val updatedBook: StateFlow<UiState<String>>
    fun fetchItemBook(bookId: String)
    fun updateItemBook(book: Book, id: String)
    fun deleteItemBook(documentId: String)
}