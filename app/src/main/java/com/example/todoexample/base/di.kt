package com.example.todoexample.base

import androidx.room.Room
import com.example.todoexample.base.database.MyDatabase
import com.example.todoexample.base.database.TaskRepository
import com.example.todoexample.card.TaskCardMode
import com.example.todoexample.card.TaskCardViewModel
import com.example.todoexample.list.ViewModelList
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diModule = module {
    single { Room.databaseBuilder(androidApplication(), MyDatabase::class.java, "database").build() }
    single { get<MyDatabase>().taskDAO() }
    single { TaskRepository(taskDao = get()) }
    single { ResourceProvider(application = get()) }

    viewModel { ViewModelList(repository = get(), application = get()) }
    viewModel { (taskCardMode: TaskCardMode) ->
        TaskCardViewModel(resourceProvider = get(), repository = get(), taskCardMode = taskCardMode)
    }
}
