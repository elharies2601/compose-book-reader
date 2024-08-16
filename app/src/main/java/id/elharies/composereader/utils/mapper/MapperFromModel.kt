package id.elharies.composereader.utils.mapper

import id.elharies.composereader.model.Book
import id.elharies.composereader.model.BookRes
import id.elharies.composereader.model.User
import id.elharies.composereader.utils.constans.KeyColumn
import id.elharies.composereader.utils.extension.toString
import java.util.Date

fun User.toMap() = hashMapOf(
    KeyColumn.userId to userId,
    KeyColumn.displayName to displayName,
    KeyColumn.avatarUrl to avatarUrl,
    KeyColumn.quote to quote,
    KeyColumn.profession to profession
)

fun BookRes.Item.toMap() = Book(
    bookId = this.id,
    title = this.volumeInfo.title,
    author = this.volumeInfo.authors.joinToString(","),
    date = this.volumeInfo.publishedDate ?: Date().toString("yyyy-MM-dd"),
    linkImage = this.volumeInfo.imageLinks.thumbnail,
    desc = this.volumeInfo.description ?: "${this.volumeInfo.title} ${this.volumeInfo.publisher}"
)