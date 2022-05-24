package nktns.todo.task.list

sealed class TaskListAction {
    class ShowCreateBottomSheet(val catalogId: Int) : TaskListAction()
    class ShowViewBottomSheet(val taskId: Int) : TaskListAction()
}
