package nktns.todo.catalog.card.bottom

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CatalogCardBottomMode : Parcelable {
    @Parcelize
    object Create : CatalogCardBottomMode()

    @Parcelize
    data class View(val catalogId: Int) : CatalogCardBottomMode()
}

