package nktns.todo.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.base.database.entity.TaskEntity

sealed class TaskListState {
    object InitialLoading : TaskListState()

    data class Content(val taskList: List<TaskEntity>, val diffResult: DiffUtil.DiffResult) : TaskListState()
}
