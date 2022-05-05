package com.example.todoexample.base.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int,
    val taskParentId: Int,
    val taskName: String,
    val description: String,
    val creationDate: Long,
    val taskCompletionDate: Long,
    val isOverdue: Boolean,
    val isCompleted: Boolean
)
