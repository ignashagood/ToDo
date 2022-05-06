package nktns.todo.base.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nktns.todo.base.database.dao.TaskDAO
import nktns.todo.base.database.dao.TaskDAONew
import nktns.todo.base.database.dao.TaskListDAONew
import nktns.todo.base.database.entity.Task
import nktns.todo.base.database.entity.TaskEntity
import nktns.todo.base.database.entity.TaskList

@Database(entities = [TaskEntity::class, Task::class, TaskList::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO
    abstract fun taskDAONew(): TaskDAONew
    abstract fun taskListDAONew(): TaskListDAONew
}
