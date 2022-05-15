package nktns.todo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nktns.todo.data.database.dao.CatalogDAO
import nktns.todo.data.database.dao.TaskDAO
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.entity.TaskEntity

@Database(entities = [TaskEntity::class, CatalogEntity::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO
    abstract fun catalogDAO(): CatalogDAO
}
