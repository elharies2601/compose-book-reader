package id.elharies.composereader.screen.search

import androidx.lifecycle.ViewModel
import id.elharies.composereader.model.BookRes
import id.elharies.composereader.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSearchViewModel: ViewModel(), ISearchViewModel {
    override val listBook: StateFlow<UiState<BookRes>>
        get() = MutableStateFlow(UiState.Idle)

    override fun searchBookByTitle(title: String) {

    }
}