package bagus2x.myhabit.presentation.main.profile

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay
import timber.log.Timber

class ProgressWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    companion object {
        const val Progress = "Progress"
        private const val delayDuration = 1L
    }

    override suspend fun doWork(): Result {
        Timber.i("HASIL LAGI")
        repeat(1000) {
            setProgress(workDataOf(Progress to it))
            Timber.i("HASIL WKWKWK $it")
            delay(1000)
        }
        return Result.success()
    }
}
