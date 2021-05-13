package ng.mint.ocrscanner.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(indices = [Index(value = ["bin"], unique = true)])
data class RecentCard(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int? = null,

    @ColumnInfo(name = "bin")
    var bin: String? = null,

    @ColumnInfo(name = "scheme")
    var scheme: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @ColumnInfo(name = "emoji")
    var emoji: String? = null,

    @ColumnInfo(name = "country")
    var country: String? = null,

    @ColumnInfo(name = "bank")
    var bank: String? = null,

    @ColumnInfo(name = "currency")
    var currency: String? = null,

    @ColumnInfo(name = "date_created")
    var dateCreated: String? = null

):Parcelable