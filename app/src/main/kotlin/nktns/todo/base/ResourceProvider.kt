package nktns.todo.base

import android.app.Application
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

class ResourceProvider(private val application: Application) {
    fun getString(@StringRes resId: Int) = application.getString(resId)

    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any) =
        application.resources.getQuantityString(resId, quantity, *formatArgs)
}
