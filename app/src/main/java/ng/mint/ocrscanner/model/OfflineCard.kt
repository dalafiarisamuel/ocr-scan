package ng.mint.ocrscanner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["bin"], unique = true)])
data class OfflineCard(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int? = null,

    @ColumnInfo(name = "bin")
    var bin: String? = null,

    @ColumnInfo(name = "date_created")
    var dateCreated: String? = null
)
