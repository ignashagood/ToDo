package nktns.todo.base.pickers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PickedTime(val hour: Int, val minute: Int) : Parcelable
