package nktns.todo.catalog.editor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import nktns.todo.data.database.entity.CatalogEntity

sealed class CatalogEditorMode : Parcelable {
    @Parcelize
    object Create : CatalogEditorMode()

    @Parcelize
    data class View(val catalog: CatalogEntity) : CatalogEditorMode()
}

