package id.elharies.composereader.utils.extension

import android.content.Context
import androidx.core.text.HtmlCompat
import coil.request.ImageRequest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(f: String, locale: Locale = Locale.getDefault()): Date {
    return try {
        val simpleDateFormat = SimpleDateFormat(f, locale)
        simpleDateFormat.parse(this) ?: Date()
    } catch (e: ParseException) {
        Date()
    }
}

fun String.buildImageReq(context: Context): ImageRequest {
    return ImageRequest.Builder(context).crossfade(true).data(this).build()
}

fun String.toTextFromHtml(): String {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}