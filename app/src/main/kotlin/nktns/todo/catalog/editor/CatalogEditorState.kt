package nktns.todo.catalog.editor

sealed class CatalogEditorState {
    object InitialLoading : CatalogEditorState()

    data class Content(
        val catalogName: String,
        val hideCompletedTasks: Boolean,
        val completedActionName: String,
        val canDelete: Boolean
    ) : CatalogEditorState()
}
