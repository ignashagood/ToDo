package nktns.todo.task.card

import java.util.Date

sealed class TaskCardAction {
    object Dismiss : TaskCardAction()

    class ShowDatePicker(val date: Date) : TaskCardAction()
    class ShowTimePicker(val time: Date) : TaskCardAction()
}
