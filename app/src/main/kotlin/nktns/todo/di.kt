package nktns.todo

import androidx.room.Room
import nktns.todo.base.ResourceProvider
import nktns.todo.catalog.card.bottom.CatalogCardBottomMode
import nktns.todo.catalog.card.bottom.CatalogCardBottomVM
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
    single { ResourceProvider(application = get()) }

    single { Room.databaseBuilder(androidApplication(), TasksDatabase::class.java, "database").build() }
    single { get<TasksDatabase>().taskDAO() }
    single { get<TasksDatabase>().catalogDAO() }

    single { TaskRepository(taskDAO = get()) }
    single { CatalogRepository(catalogDAO = get()) }

    viewModel { TaskListVM(application = get(), repository = get()) }
    viewModel { (taskCardMode: TaskCardMode) ->
        TaskCardVM(resourceProvider = get(), repository = get(), taskCardMode = taskCardMode)
    }
    viewModel { (catalogCardBottomMode: CatalogCardBottomMode) ->
        CatalogCardBottomVM(resourceProvider = get(), repository = get(), mode = catalogCardBottomMode)
    }
    viewModel { CatalogListVM(application = get(), repository = get()) }
}
