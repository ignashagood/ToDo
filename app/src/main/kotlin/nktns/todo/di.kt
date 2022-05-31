package nktns.todo

import androidx.room.Room
import nktns.todo.base.ResourceProvider
import nktns.todo.catalog.card.CatalogCardVM
import nktns.todo.catalog.editor.CatalogEditorMode
import nktns.todo.catalog.editor.CatalogEditorVM
import nktns.todo.catalog.list.CatalogListVM
import nktns.todo.data.CatalogRepository
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.TasksDatabase
import nktns.todo.task.card.TaskCardMode
import nktns.todo.task.card.TaskCardVM
import nktns.todo.task.list.TaskListMode
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

    viewModel { (taskListMode: TaskListMode) ->
        TaskListVM(application = get(), taskRepository = get(), taskListMode = taskListMode)
    }
    viewModel { (taskCardMode: TaskCardMode) ->
        TaskCardVM(
            resourceProvider = get(),
            taskRepository = get(),
            catalogRepository = get(),
            taskCardMode = taskCardMode
        )
    }
    viewModel { (catalogEditorMode: CatalogEditorMode) ->
        CatalogEditorVM(resourceProvider = get(), repository = get(), mode = catalogEditorMode)
    }
    viewModel { CatalogListVM(application = get(), repository = get()) }
    viewModel { CatalogCardVM(repository = get(), catalogId = get()) }
}
