package nktns.todo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

const val CATALOG_ID = "catalogId"
const val CATALOG_NAME = "catalogName"

@Entity(tableName = "catalogs")
data class Catalog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CATALOG_ID) val id: Int,
    @ColumnInfo(name = CATALOG_NAME) val name: String,
    val creationDate: Date
)
