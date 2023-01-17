package nktns.todo.task.card

import nktns.todo.base.pickers.PickedDate
import nktns.todo.base.pickers.PickedTime
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.entity.TaskEntity

sealed class TaskCardAction {
    object Dismiss : TaskCardAction()

    object DismissCatalogPicker : TaskCardAction()

    class ShowDatePicker(val date: PickedDate) : TaskCardAction()

    class ShowTimePicker(val time: PickedTime) : TaskCardAction()

    class ShowCatalogPicker(val catalogs: List<CatalogEntity>) : TaskCardAction()

    class ScheduleNotification(val task: TaskEntity) : TaskCardAction()
}

