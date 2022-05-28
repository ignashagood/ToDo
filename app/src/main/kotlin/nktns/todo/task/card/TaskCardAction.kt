package nktns.todo.task.card

import nktns.todo.base.pickers.PickedDate
import nktns.todo.base.pickers.PickedTime

sealed class TaskCardAction {
    object Dismiss : TaskCardAction()

    class ShowDatePicker(val date: PickedDate) : TaskCardAction()

    class ShowTimePicker(val time: PickedTime) : TaskCardAction()
}
