package nktns.todo.task.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class TaskListMode : Parcelable {
    @Parcelize
    object All : TaskListMode()

    @Parcelize
    object Today : TaskListMode()

    @Parcelize
    data class Catalog(val catalogId: Int) : TaskListMode()
}
