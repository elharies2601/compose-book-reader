package id.elharies.composereader.utils.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toString(f: String): String {
    val simpleDateFormat = SimpleDateFormat(f, Locale.getDefault())
    return simpleDateFormat.format(this)
}