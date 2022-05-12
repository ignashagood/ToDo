package nktns.todo.task.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.data.database.entity.TaskEntity

sealed class TaskListState {
    object InitialLoading : TaskListState()

    data class Content(val taskList: List<TaskEntity>, val diffResult: DiffUtil.DiffResult) : TaskListState()
}
