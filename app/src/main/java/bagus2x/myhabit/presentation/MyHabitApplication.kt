package bagus2x.myhabit.presentation

import android.app.Application
import bagus2x.myhabit.BuildConfig
import bagus2x.myhabit.presentation.task.TaskNotificationWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyHabitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // Register notification channel
        TaskNotificationWorker.registerNotificationChannel(this)
    }
}
