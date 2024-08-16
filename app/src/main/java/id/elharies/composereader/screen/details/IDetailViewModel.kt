package id.elharies.composereader.screen.details

import id.elharies.composereader.model.Book
import id.elharies.composereader.model.BookRes
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.StateFlow

interface IDetailViewModel {
    val detailBook: StateFlow<UiState<BookRes.Item>>
    val saveBook: StateFlow<UiState<String>>
    fun getDetailBook(idBook: String)
    fun saveBookToDb(book: Book)
}