package nktns.todo.base

import androidx.room.Room
import nktns.todo.base.database.TasksDatabase
import nktns.todo.base.database.TaskRepository
import nktns.todo.card.TaskCardMode
import nktns.todo.card.TaskCardVM
import nktns.todo.list.TaskListVM
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diModule = module {
    single { Room.databaseBuilder(androidApplication(), TasksDatabase::class.java, "database").build() }
    single { get<TasksDatabase>().taskDAO() }
    single { TaskRepository(taskDao = get()) }
    single { ResourceProvider(application = get()) }

    viewModel { TaskListVM(repository = get(), application = get()) }
    viewModel { (taskCardMode: TaskCardMode) ->
        TaskCardVM(resourceProvider = get(), repository = get(), taskCardMode = taskCardMode)
    }
}
