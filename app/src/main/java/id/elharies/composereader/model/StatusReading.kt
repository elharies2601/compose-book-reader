package id.elharies.composereader.model

import android.os.Parcelable
import id.elharies.composereader.utils.constans.ConstStatusReading
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class StatusReading(var status: String) : Parcelable {
    data object NotStarted: StatusReading(ConstStatusReading.notStarted)
    data object Reading: StatusReading(ConstStatusReading.reading)
    data object Finished: StatusReading(ConstStatusReading.finished)
}