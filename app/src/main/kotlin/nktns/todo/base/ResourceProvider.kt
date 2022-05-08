package nktns.todo.base

import android.app.Application
import androidx.annotation.StringRes

//Здесь именно Application, а не любой Context, т.к. только Application живёт всё время, его утечки не будет
//ResourceProvider мы кладём в koin как single
//Если бы здесь был Context, то можно было бы передать какую-нибудь активность в качестве него, а это будет утечка
//Нужно пресекать потенциальные проблемы на уровне объявления, а не просто договорённостями на словах
class ResourceProvider(private val application: Application) {
    fun getString(@StringRes resId: Int) = application.getString(resId)
}
