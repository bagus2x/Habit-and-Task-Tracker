package bagus2x.myhabit.presentation.task

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import bagus2x.myhabit.R
import bagus2x.myhabit.presentation.main.MainActivity

class TaskNotificationWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val notificationManager = context.getSystemService<NotificationManager>()
        val id = inputData.getLong(KEY_TASK_ID, 0).toInt()
        val title = inputData.getString(KEY_TASK_TITLE)
        val description = inputData.getString(KEY_TASK_DESC)
        if (notificationManager != null && id != 0 && title != null && description != null) {
            notificationManager.showNotification(id, title, description)
            return Result.success()
        }
        return Result.success()
    }

    private fun NotificationManager.showNotification(id: Int, title: String, description: String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        // TODO: Add pending intent and deep link
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_alarm)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(activityPendingIntent)
            .build()
        notify(id, notification)
    }

    companion object {
        const val KEY_TASK_ID = "task_id"
        const val KEY_TASK_TITLE = "task_title"
        const val KEY_TASK_DESC = "task_desc"
        private const val CHANNEL_ID = "task_channel_id"

        fun registerNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.text_task),
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.description = context.getString(R.string.channel_task_desc)
                val notificationManager = context.getSystemService<NotificationManager>()
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }
}
