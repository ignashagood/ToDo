package nktns.todo.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "catalogs")
data class CatalogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "catalogId") val id: Int,
    @ColumnInfo(name = "catalogName") val name: String,
    @ColumnInfo(name = "catalogCreationDate") val creationDate: Date
) : Parcelable


