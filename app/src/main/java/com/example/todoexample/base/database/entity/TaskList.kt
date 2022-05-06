package com.example.todoexample.base.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TASK_LIST_ID = "taskListId"
const val TASK_LIST_NAME = "taskListName"

@Entity(tableName = "taskLists")
data class TaskList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TASK_LIST_ID) val id: Int,
    @ColumnInfo(name = TASK_LIST_NAME) val name: String,
    val creationDate: Long
)
