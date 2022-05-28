package nktns.todo.base

import android.util.Log
import nktns.todo.BuildConfig
import kotlin.system.exitProcess

fun illegalState(reason: String) {
    Log.e("ERROR", "", IllegalStateException(reason))
    if (BuildConfig.DEBUG) {
        exitProcess(-1)
    }
}
