package id.elharies.composereader.screen.search

import id.elharies.composereader.model.BookRes
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.StateFlow

interface ISearchViewModel {
    val listBook: StateFlow<UiState<BookRes>>
    fun searchBookByTitle(title: String)
}