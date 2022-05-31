package nktns.todo.base

import android.app.Application
import androidx.annotation.StringRes

class ResourceProvider(private val application: Application) {
    fun getString(@StringRes resId: Int) = application.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any) = application.getString(resId, *formatArgs)
}
