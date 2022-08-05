package bagus2x.myhabit.di

import android.content.Context
import bagus2x.myhabit.data.DefaultConnectivityRepository
import bagus2x.myhabit.data.DefaultHabitRepository
import bagus2x.myhabit.data.DefaultHistoryRepository
import bagus2x.myhabit.data.DefaultTaskRepository
import bagus2x.myhabit.data.local.HabitLocalDataSource
import bagus2x.myhabit.data.local.HistoryLocalDataSource
import bagus2x.myhabit.data.local.TaskLocalDataSource
import bagus2x.myhabit.domain.repository.ConnectivityRepository
import bagus2x.myhabit.domain.repository.HabitRepository
import bagus2x.myhabit.domain.repository.HistoryRepository
import bagus2x.myhabit.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHabitRepository(habitLocalDataSource: HabitLocalDataSource): HabitRepository {
        return DefaultHabitRepository(habitLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(historyLocalDataSource: HistoryLocalDataSource): HistoryRepository {
        return DefaultHistoryRepository(historyLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskLocalDataSource: TaskLocalDataSource): TaskRepository {
        return DefaultTaskRepository(taskLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideConnectivityRepository(
        @ApplicationContext
        context: Context
    ): ConnectivityRepository {
        return DefaultConnectivityRepository(context)
    }
}
