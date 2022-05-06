package nktns.todo.card

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class TaskCardMode : Parcelable {

    @Parcelize
    object Create : TaskCardMode()

    @Parcelize
    data class View(val taskId: Int) : TaskCardMode()
}
