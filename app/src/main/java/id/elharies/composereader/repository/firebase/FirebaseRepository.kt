package id.elharies.composereader.repository.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import id.elharies.composereader.model.Book

interface FirebaseRepository {
    fun createUser(email: String, password: String): Task<AuthResult>
    fun loginUser(email: String, password: String): Task<AuthResult>
    fun signOutUser()
    suspend fun saveUserIntoDb(fullName: String, uid: String)
    suspend fun isLogin(): Boolean
    suspend fun saveBookIntoDb(book: Book): Task<DocumentReference>
    fun updateBook(payload: Map<String, Any?>, id: String): Task<Void>
    fun getAllBooks(): CollectionReference
    fun getBooksByUser(): Query
    suspend fun getBook(bookId: String): List<DocumentSnapshot>
    suspend fun deleteBook(documentId: String): Task<Void>
    fun getCurrentUser(): FirebaseUser?
}