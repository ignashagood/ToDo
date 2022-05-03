package com.example.todoexample.base.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    val isCompleted: Boolean
)
