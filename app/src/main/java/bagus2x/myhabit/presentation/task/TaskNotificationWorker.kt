package bagus2x.myhabit.presentation.task

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class TaskNotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        return Result.success()
    }

    companion object {
        const val KEY_TASK_ID = "task_id"
    }
}
