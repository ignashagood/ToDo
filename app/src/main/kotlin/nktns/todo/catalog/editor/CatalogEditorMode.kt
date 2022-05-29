package nktns.todo.catalog.editor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CatalogEditorMode : Parcelable {
    @Parcelize
    object Create : CatalogEditorMode()

    @Parcelize
    data class View(val catalogId: Int) : CatalogEditorMode()
}

