package id.elharies.composereader.repository.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import id.elharies.composereader.model.Book
import id.elharies.composereader.model.User
import id.elharies.composereader.utils.constans.KeyColumn.userId
import id.elharies.composereader.utils.constans.Path.books
import id.elharies.composereader.utils.constans.Path.users
import id.elharies.composereader.utils.mapper.toMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) :
    FirebaseRepository {
    override fun createUser(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    override fun loginUser(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun isLogin(): Boolean {
        return withContext(Dispatchers.IO) {
            auth.currentUser != null
        }
    }

    override suspend fun saveUserIntoDb(fullName: String, uid: String) {
        val model = User(displayName = fullName, userId = uid)
        firestore.collection(users).add(model.toMap())
    }

    override fun signOutUser() {
        auth.signOut()
    }

    override suspend fun saveBookIntoDb(book: Book): Task<DocumentReference> {
        val uid = auth.currentUser?.uid
        return firestore.collection(books).add(book.copy(userId = uid))
    }

    override fun updateBook(payload: Map<String, Any?>, id: String): Task<Void> {
        return firestore.collection(books).document(id)
            .update(payload)
    }

    override fun getAllBooks(): CollectionReference {
        return firestore.collection(books)
    }

    override fun getBooksByUser(): Query {
        val id = auth.currentUser?.uid
        return firestore.collection(books).whereEqualTo(userId, id)
    }

    override suspend fun getBook(bookId: String): List<DocumentSnapshot> {
        return withContext(Dispatchers.IO) {
            firestore.collection(books).whereEqualTo("bookId", bookId).get().await().documents
        }
    }

    override suspend fun deleteBook(documentId: String): Task<Void> {
        return firestore.collection(books).document(documentId).delete()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}