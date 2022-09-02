package nktns.todo.base

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.RED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import nktns.todo.R
import nktns.todo.main.MainActivity
import org.koin.java.KoinJavaComponent.getKoin

class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        const val NOTIFICATION_ID = "nktnsToDo_notification_id"
        const val NOTIFICATION_NAME = "nktnsToDo"
        const val NOTIFICATION_CHANNEL = "nktnsToDo_channel_01"
        const val NOTIFICATION_WORK = "nktnsToDo_notification_work"
        const val NOTIFICATION_TITLE = "notification_title"
        const val NOTIFICATION_SUBTITLE = "notification_subtitle"
    }

    private val eventBus: EventBus = getKoin().get()

    override fun doWork(): Result {
        val id = inputData.getInt(NOTIFICATION_ID, 0)
        sendNotification(id)
        eventBus.emitEvent(AppEvent.UpdateTaskList)
        return success()
    }

    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)
        val pendingIntent = getActivity(applicationContext, 0, intent, 0)

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_notification_time_24)
            .setContentTitle(inputData.getString(NOTIFICATION_TITLE))
            .setContentText(inputData.getString(NOTIFICATION_SUBTITLE))
            .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = RED
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }
}
