package nktns.todo

import androidx.room.Room
import nktns.todo.base.ResourceProvider
import nktns.todo.catalog.list.CatalogListVM
import nktns.todo.data.CatalogRepository
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.TasksDatabase
import nktns.todo.task.card.TaskCardMode
import nktns.todo.task.card.TaskCardVM
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
    single { CatalogRepository(catalogDAO = get()) }

    viewModel { TaskListVM(application = get(), repository = get()) }
    viewModel { (taskCardMode: TaskCardMode) ->
        TaskCardVM(resourceProvider = get(), repository = get(), taskCardMode = taskCardMode)
    }
    viewModel { CatalogListVM(application = get(), repository = get()) }
}
