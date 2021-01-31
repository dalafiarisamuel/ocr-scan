package ng.mint.ocrscanner.model

import com.google.gson.annotations.SerializedName

class CardResponse {

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("scheme")
    var scheme: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("reason")
    var reason: String? = null

    @SerializedName("category")
    var category: String? = null

    @SerializedName("number")
    var number: Number? = null

    @SerializedName("country")
    var country: Country? = null

    @SerializedName("bank")
    var bank: Bank? = null


    inner class Number {

        @SerializedName("iin")
        var lin: String? = null

        @SerializedName("length")
        var length: Int? = null

        @SerializedName("luhn")
        var luhn: Boolean? = null
    }

    inner class Country {

        @SerializedName("alpha2")
        var alpha2: String? = null

        @SerializedName("alpha3")
        var alpha3: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("emoji")
        var emoji: String? = null

        @SerializedName("currency")
        var currency:String? = null
    }

    inner class Bank {

        @SerializedName("name")
        var name: String? = null

        @SerializedName("phone")
        var phone: String? = null

        @SerializedName("url")
        var url: String? = null


    }
}