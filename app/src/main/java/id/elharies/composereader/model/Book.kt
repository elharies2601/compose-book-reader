package id.elharies.composereader.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    var id: String? = null,
    var title: String = "",
    var author: String = "",
    var notes: String? = null,
    var date: String = "",
    var rating: Double = 0.0,
    var linkImage: String = "",
    var desc: String = "",
    var startedReading: Timestamp? = null,
    var finishedReading: Timestamp? = null,
    var userId: String? = null,
    var bookId: String? = null,
    var statusReading: String = StatusReading.NotStarted.status
): Parcelable
