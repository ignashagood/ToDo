package nktns.todo.task.card

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import nktns.todo.data.database.entity.CatalogEntity

sealed class TaskCardMode : Parcelable {
    @Parcelize
    data class Create(val catalog: CatalogEntity?) : TaskCardMode()

    @Parcelize
    data class View(val taskId: Int) : TaskCardMode()
}
