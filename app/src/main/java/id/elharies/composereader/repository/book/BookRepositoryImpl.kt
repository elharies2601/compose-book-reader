package id.elharies.composereader.repository.book

import id.elharies.composereader.model.BookRes
import id.elharies.composereader.network.BookApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val api: BookApi) : BookRepository {
    override suspend fun searchBook(title: String): BookRes {
        return withContext(Dispatchers.IO) {
            api.getBook(title)
        }
    }

    override suspend fun fetchDetailBook(idBook: String): BookRes.Item {
        return withContext(Dispatchers.IO) {
            api.getDetailBook(idBook)
        }
    }
}