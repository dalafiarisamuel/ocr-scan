package ng.mint.ocrscanner

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.views.activities.MainActivity
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


fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.capitalize(Locale.getDefault()) }

fun Context.showBinNotification(bin: String) {

    val message = String.format(getString(R.string.offline_card_result_notification_text), bin)
    val channel = "ocr_scanner_id"
    val groupKey = "ng.mint.ocrscanner.offline.result"

    // intent to clear the notification
    val contentIntent = NavDeepLinkBuilder(this)
        .setGraph(R.navigation.nav_graph)
        .setComponentName(MainActivity::class.java)
        .setDestination(R.id.recentlyViewedCardsFragment)
        //.setArguments(args)
        .createPendingIntent()

    val builder = NotificationCompat.Builder(this, channel)
    val notificationManager =
        this.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    //Notification channel should only be created for devices running Android 26
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel = NotificationChannel(
            channel,
            getString(R.string.app_name),
            importance
        )
        //Boolean value to set if lights are enabled for Notifications from this Channel
        notificationChannel.enableLights(true)
        //Boolean value to set if vibration are enabled for Notifications from this Channel
        notificationChannel.enableVibration(true)
        //Sets the color of Notification Light
        notificationChannel.lightColor = Color.BLACK
        //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
        notificationChannel.vibrationPattern = longArrayOf(
            500,
            500,
            500,
            500,
            500
        )
        //Sets whether notifications from these Channel should be visible on Lockscreen or not
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        builder.setChannelId(channel)
        notificationManager?.createNotificationChannel(notificationChannel)
    }

    builder.setSmallIcon(R.drawable.ic_credit_card) // this is normal image

    builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
    builder.setContentTitle(getString(R.string.offline_scan_result))
    builder.setContentText(message)
    builder.setGroupSummary(true)
    builder.setAutoCancel(true)
    builder.setGroup(groupKey)
    builder.setColorized(true)
    builder.priority = NotificationCompat.PRIORITY_DEFAULT
    builder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
    builder.setContentIntent(contentIntent)
    builder.addAction(R.drawable.ic_credit_card, getString(R.string.view_detail), contentIntent)

    notificationManager?.notify(System.currentTimeMillis().toInt(), builder.build())

}



