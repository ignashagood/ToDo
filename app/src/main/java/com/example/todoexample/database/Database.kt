package com.example.todoexample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoexample.database.dao.TaskDAO
import com.example.todoexample.database.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun taskDAO(): TaskDAO

    companion object {
        private var INSTANCE: MyDatabase? = null

        fun get(context: Context): MyDatabase {
            if (INSTANCE == null) INSTANCE = Room.databaseBuilder(context, MyDatabase::class.java, "database").build()
            return INSTANCE as MyDatabase
        }

    }


}