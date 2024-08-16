package id.elharies.composereader.network

import com.google.gson.Gson
import id.elharies.composereader.model.BookRes
import id.elharies.composereader.utils.constans.KeyArguments
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface BookApi {
    @GET("v1/volumes")
    suspend fun getBook(@Query("q") book: String = "Android"): BookRes

    @GET("v1/volumes/{${KeyArguments.keyBook}}") // Wh_xDwAAQBAJ
    suspend fun getDetailBook(@Path(KeyArguments.keyBook) idBook: String): BookRes.Item

    class Creator {
        @Inject
        fun newInstance(gson: Gson, okhttp: OkHttpClient): BookApi {
            return Retrofit.Builder().baseUrl("https://www.googleapis.com/books/")
                .client(okhttp)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(BookApi::class.java)
        }
    }
}