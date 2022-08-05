package bagus2x.myhabit.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import bagus2x.myhabit.data.local.HabitLocalDataSource
import bagus2x.myhabit.data.local.HistoryLocalDataSource
import bagus2x.myhabit.data.local.TaskLocalDataSource
import bagus2x.myhabit.data.local.room.MyHabitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideMyHabitDatabase(
        @ApplicationContext
        context: Context
    ): MyHabitDatabase {
        return Room
            .databaseBuilder(context, MyHabitDatabase::class.java, "my_habit.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @Singleton
    fun provideHabitLocalDataSource(myHabitDatabase: MyHabitDatabase): HabitLocalDataSource {
        return HabitLocalDataSource(myHabitDatabase.habitDao)
    }

    @Provides
    @Singleton
    fun provideHistoryLocalDataSource(myHabitDatabase: MyHabitDatabase): HistoryLocalDataSource {
        return HistoryLocalDataSource(myHabitDatabase.historyDao)
    }

    @Provides
    @Singleton
    fun provideTaskLocalDataSource(myHabitDatabase: MyHabitDatabase): TaskLocalDataSource {
        return TaskLocalDataSource(myHabitDatabase.taskDao)
    }

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext
        context: Context
    ): WorkManager {
        return WorkManager.getInstance(context)
    }
}
