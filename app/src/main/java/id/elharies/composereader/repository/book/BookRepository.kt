package id.elharies.composereader.repository.book

import id.elharies.composereader.model.BookRes
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBook(title: String = "android"): BookRes
    suspend fun fetchDetailBook(idBook: String): BookRes.Item
}