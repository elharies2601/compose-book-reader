package id.elharies.composereader.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BookRes(
    @SerializedName("items")
    var items: List<Item> = listOf(),
    @SerializedName("kind")
    var kind: String = "",
    @SerializedName("totalItems")
    var totalItems: Int = 0
) {
    @Keep
    data class Item(
        @SerializedName("accessInfo")
        var accessInfo: AccessInfo = AccessInfo(),
        @SerializedName("etag")
        var etag: String = "",
        @SerializedName("id")
        var id: String = "",
        @SerializedName("kind")
        var kind: String = "",
        @SerializedName("saleInfo")
        var saleInfo: SaleInfo = SaleInfo(),
        @SerializedName("searchInfo")
        var searchInfo: SearchInfo = SearchInfo(),
        @SerializedName("selfLink")
        var selfLink: String = "",
        @SerializedName("volumeInfo")
        var volumeInfo: VolumeInfo = VolumeInfo(),
        @SerializedName("error")
        var error: BookFailRes.Error? = null
    ) {
        @Keep
        data class AccessInfo(
            @SerializedName("accessViewStatus")
            var accessViewStatus: String = "",
            @SerializedName("country")
            var country: String = "",
            @SerializedName("embeddable")
            var embeddable: Boolean = false,
            @SerializedName("epub")
            var epub: Epub = Epub(),
            @SerializedName("pdf")
            var pdf: Pdf = Pdf(),
            @SerializedName("publicDomain")
            var publicDomain: Boolean = false,
            @SerializedName("quoteSharingAllowed")
            var quoteSharingAllowed: Boolean = false,
            @SerializedName("textToSpeechPermission")
            var textToSpeechPermission: String = "",
            @SerializedName("viewability")
            var viewability: String = "",
            @SerializedName("webReaderLink")
            var webReaderLink: String = ""
        ) {
            @Keep
            data class Epub(
                @SerializedName("isAvailable")
                var isAvailable: Boolean = false
            )

            @Keep
            data class Pdf(
                @SerializedName("acsTokenLink")
                var acsTokenLink: String = "",
                @SerializedName("isAvailable")
                var isAvailable: Boolean = false
            )
        }

        @Keep
        data class SaleInfo(
            @SerializedName("buyLink")
            var buyLink: String? = null,
            @SerializedName("country")
            var country: String = "",
            @SerializedName("isEbook")
            var isEbook: Boolean = false,
            @SerializedName("listPrice")
            var listPrice: ListPrice? = null,
            @SerializedName("offers")
            var offers: List<Offer>? = null,
            @SerializedName("retailPrice")
            var retailPrice: RetailPrice? = null,
            @SerializedName("saleability")
            var saleability: String = ""
        ) {
            @Keep
            data class ListPrice(
                @SerializedName("amount")
                var amount: Int = 0,
                @SerializedName("currencyCode")
                var currencyCode: String = ""
            )

            @Keep
            data class Offer(
                @SerializedName("finskyOfferType")
                var finskyOfferType: Int = 0,
                @SerializedName("listPrice")
                var listPrice: ListPrice = ListPrice(),
                @SerializedName("retailPrice")
                var retailPrice: RetailPrice = RetailPrice()
            ) {
                @Keep
                data class ListPrice(
                    @SerializedName("amountInMicros")
                    var amountInMicros: Long = 0,
                    @SerializedName("currencyCode")
                    var currencyCode: String = ""
                )

                @Keep
                data class RetailPrice(
                    @SerializedName("amountInMicros")
                    var amountInMicros: Long = 0,
                    @SerializedName("currencyCode")
                    var currencyCode: String = ""
                )
            }

            @Keep
            data class RetailPrice(
                @SerializedName("amount")
                var amount: Int = 0,
                @SerializedName("currencyCode")
                var currencyCode: String = ""
            )
        }

        @Keep
        data class SearchInfo(
            @SerializedName("textSnippet")
            var textSnippet: String = ""
        )

        @Keep
        data class VolumeInfo(
            @SerializedName("allowAnonLogging")
            var allowAnonLogging: Boolean = false,
            @SerializedName("authors")
            var authors: List<String> = listOf(),
            @SerializedName("averageRating")
            var averageRating: Int? = null,
            @SerializedName("canonicalVolumeLink")
            var canonicalVolumeLink: String = "",
            @SerializedName("categories")
            var categories: List<String>? = null,
            @SerializedName("contentVersion")
            var contentVersion: String = "",
            @SerializedName("description")
            var description: String? = null,
            @SerializedName("imageLinks")
            var imageLinks: ImageLinks = ImageLinks(),
            @SerializedName("industryIdentifiers")
            var industryIdentifiers: List<IndustryIdentifier>? = null,
            @SerializedName("infoLink")
            var infoLink: String = "",
            @SerializedName("language")
            var language: String = "",
            @SerializedName("maturityRating")
            var maturityRating: String = "",
            @SerializedName("pageCount")
            var pageCount: Int = 0,
            @SerializedName("panelizationSummary")
            var panelizationSummary: PanelizationSummary = PanelizationSummary(),
            @SerializedName("previewLink")
            var previewLink: String = "",
            @SerializedName("printType")
            var printType: String = "",
            @SerializedName("publishedDate")
            var publishedDate: String? = null,
            @SerializedName("publisher")
            var publisher: String = "",
            @SerializedName("ratingsCount")
            var ratingsCount: Int? = null,
            @SerializedName("readingModes")
            var readingModes: ReadingModes = ReadingModes(),
            @SerializedName("subtitle")
            var subtitle: String? = null,
            @SerializedName("title")
            var title: String = ""
        ) {
            @Keep
            data class ImageLinks(
                @SerializedName("smallThumbnail")
                var smallThumbnail: String = "",
                @SerializedName("thumbnail")
                var thumbnail: String = ""
            )

            @Keep
            data class IndustryIdentifier(
                @SerializedName("identifier")
                var identifier: String = "",
                @SerializedName("type")
                var type: String = ""
            )

            @Keep
            data class PanelizationSummary(
                @SerializedName("containsEpubBubbles")
                var containsEpubBubbles: Boolean = false,
                @SerializedName("containsImageBubbles")
                var containsImageBubbles: Boolean = false
            )

            @Keep
            data class ReadingModes(
                @SerializedName("image")
                var image: Boolean = false,
                @SerializedName("text")
                var text: Boolean = false
            )
        }
    }
}