package nktns.todo.base

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class EventBus {
    private val _events = MutableSharedFlow<AppEvent>(extraBufferCapacity = 1)
    val events: Flow<AppEvent> by ::_events

    fun emitEvent(event: AppEvent) {
        Log.d(TAG, "Emitting event = $event")
        _events.tryEmit(event)
    }

    companion object {
        private const val TAG = "EventBus"
    }
}
