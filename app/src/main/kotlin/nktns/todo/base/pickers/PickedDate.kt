package nktns.todo.base.pickers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PickedDate(val year: Int, val month: Int, val day: Int) : Parcelable
