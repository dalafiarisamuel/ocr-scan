package ng.mint.ocrscanner

import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.model.RecentCard
import java.text.SimpleDateFormat
import java.util.*


fun CardResponse.toRecentCard(cardBin: String): RecentCard {

    return RecentCard(
        bin = cardBin,
        scheme = scheme,
        type = type,
        phone = bank?.phone,
        emoji = country?.emoji,
        country = country?.name,
        bank = bank?.name,
        currency = country?.currency,
        dateCreated = Date().formatToViewDateTimeDefaults()
    )
}


fun Date.formatToViewDateTimeDefaults(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}



