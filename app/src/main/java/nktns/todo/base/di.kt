package nktns.todo.base

import androidx.room.Room
import nktns.todo.base.database.CatalogRepository
import nktns.todo.base.database.TaskRepository
import nktns.todo.base.database.TasksDatabase
import nktns.todo.task.card.TaskCardMode
import nktns.todo.task.card.TaskCardVM
import nktns.todo.catalog.list.CatalogListVM
import nktns.todo.task.list.TaskListVM
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diModule = module {
    single { Room.databaseBuilder(androidApplication(), TasksDatabase::class.java, "database").build() }
    single { get<TasksDatabase>().taskDAO() }
    single { TaskRepository(taskDao = get()) }
    single { ResourceProvider(application = get()) }
    single { get<TasksDatabase>().taskListDAONew() }
    single { CatalogRepository(taskListDAONew = get()) }

    viewModel { TaskListVM(application = get(), repository = get()) }
    viewModel { (taskCardMode: TaskCardMode) ->
        TaskCardVM(resourceProvider = get(), repository = get(), taskCardMode = taskCardMode)
    }
    viewModel { CatalogListVM(application = get(), repository = get()) }
}
