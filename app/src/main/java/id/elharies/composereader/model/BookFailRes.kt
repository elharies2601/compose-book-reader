package id.elharies.composereader.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BookFailRes(
    @SerializedName("error")
    var error: Error = Error()
) {
    @Keep
    data class Error(
        @SerializedName("code")
        var code: Int = 0,
        @SerializedName("errors")
        var errors: List<Error> = listOf(),
        @SerializedName("message")
        var message: String = ""
    ) {
        @Keep
        data class Error(
            @SerializedName("domain")
            var domain: String = "",
            @SerializedName("message")
            var message: String = "",
            @SerializedName("reason")
            var reason: String = ""
        )
    }
}