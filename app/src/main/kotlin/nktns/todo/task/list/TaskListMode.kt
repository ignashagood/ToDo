package nktns.todo.task.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import nktns.todo.data.database.entity.CatalogEntity

sealed class TaskListMode : Parcelable {
    @Parcelize
    object All : TaskListMode()

    @Parcelize
    object Today : TaskListMode()

    @Parcelize
    object Archive : TaskListMode()

    @Parcelize
    data class Catalog(val catalog: CatalogEntity) : TaskListMode()
}
