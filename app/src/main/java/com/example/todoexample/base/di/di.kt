package com.example.todoexample.base.di

import androidx.room.Room
import com.example.todoexample.base.database.MyDatabase
import com.example.todoexample.base.database.TaskRepository
import com.example.todoexample.card.TaskCardMode
import com.example.todoexample.card.TaskCardViewModel
import com.example.todoexample.list.ViewModelList
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DIModule = module {

    viewModel { ViewModelList(repository = get(), application = get()) }
    viewModel { (taskCardMode: TaskCardMode) ->
        TaskCardViewModel(
            taskCardMode = taskCardMode,
            repository = get()
        )
    }

    single {
        Room.databaseBuilder(
            androidApplication(),
            MyDatabase::class.java,
            "database"
        ).build()
    }
    single { get<MyDatabase>().taskDAO() }
    single { TaskRepository(get()) }
}
