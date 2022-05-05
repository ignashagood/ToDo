package com.example.todoexample.base.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskLists")
data class TaskList(
    @PrimaryKey(autoGenerate = true)
    val taskListId: Int,
    val taskListName: String,
    val creationDate: Long
)
