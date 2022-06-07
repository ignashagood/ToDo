package nktns.todo.task.list

import nktns.todo.data.database.entity.CatalogEntity

sealed class TaskListAction {
    class ShowCreateBottomSheet(val catalog: CatalogEntity?) : TaskListAction()
    class ShowViewBottomSheet(val taskId: Int) : TaskListAction()
}
