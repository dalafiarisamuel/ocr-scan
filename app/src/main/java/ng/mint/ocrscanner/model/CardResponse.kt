package ng.mint.ocrscanner.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardResponse(
    @Json(name = "success")
    var success: Boolean? = null,

    @Json(name = "scheme")
    var scheme: String? = null,

    @Json(name = "type")
    var type: String? = null,

    @Json(name = "reason")
    var reason: String? = null,

    @Json(name = "category")
    var category: String? = null,

    @Json(name = "number")
    var number: Number? = null,

    @Json(name = "country")
    var country: Country? = null,

    @Json(name = "bank")
    var bank: Bank? = null
) {

    @JsonClass(generateAdapter = true)
    data class Number(
        @Json(name = "iin")
        var lin: String? = null,

        @Json(name = "length")
        var length: Int? = null,

        @Json(name = "luhn")
        var luhn: Boolean? = null
    )

    @JsonClass(generateAdapter = true)
    data class Country(
        @Json(name = "alpha2")
        var alpha2: String? = null,

        @Json(name = "alpha3")
        var alpha3: String? = null,

        @Json(name = "name")
        var name: String? = null,

        @Json(name = "emoji")
        var emoji: String? = null,

        @Json(name = "currency")
        var currency: String? = null
    )

    @JsonClass(generateAdapter = true)
    data class Bank(
        @Json(name = "name")
        var name: String? = null,

        @Json(name = "phone")
        var phone: String? = null,

        @Json(name = "url")
        var url: String? = null
    )
}