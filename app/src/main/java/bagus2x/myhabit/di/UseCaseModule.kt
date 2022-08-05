package bagus2x.myhabit.di

import bagus2x.myhabit.domain.repository.HabitRepository
import bagus2x.myhabit.domain.repository.HistoryRepository
import bagus2x.myhabit.domain.repository.TaskRepository
import bagus2x.myhabit.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideCreateHabitUseCase(habitRepository: HabitRepository): CreateHabitUseCase {
        return CreateHabitUseCase(habitRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateHabitUseCase(habitRepository: HabitRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(habitRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetHabitUseCase(habitRepository: HabitRepository): ObserveHabitWithHistoriesUseCase {
        return ObserveHabitWithHistoriesUseCase(habitRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveHabitsUseCase(habitRepository: HabitRepository): ObserveHabitsWithProgressUseCase {
        return ObserveHabitsWithProgressUseCase(habitRepository)
    }

    @Provides
    @ViewModelScoped
    fun deleteHabitUseCase(habitRepository: HabitRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(habitRepository)
    }

    @Provides
    @ViewModelScoped
    fun createHistoryUseCase(
        habitRepository: HabitRepository,
        historyRepository: HistoryRepository
    ): CreateHistoryUseCase {
        return CreateHistoryUseCase(habitRepository, historyRepository)
    }

    @Provides
    @ViewModelScoped
    fun deleteHistoryUseCase(historyRepository: HistoryRepository): DeleteHistoryUseCase {
        return DeleteHistoryUseCase(historyRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCreateTaskUseCase(taskRepository: TaskRepository): CreateTaskUseCase {
        return CreateTaskUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveTaskChildrenUseCase(taskRepository: TaskRepository): ObserveTaskHierarchyUseCase {
        return ObserveTaskHierarchyUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveTaskParentUseCase(taskRepository: TaskRepository): ObserveTaskParentUseCase {
        return ObserveTaskParentUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveTaskUseCase(taskRepository: TaskRepository): ObserveTaskUseCase {
        return ObserveTaskUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteTaskUseCase(taskRepository: TaskRepository): DeleteTaskUseCase {
        return DeleteTaskUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateTaskUseCase(taskRepository: TaskRepository): UpdateTaskUseCase {
        return UpdateTaskUseCase(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideObserveTaskBreadcrumbUseCase(taskRepository: TaskRepository): ObserveTaskBreadcrumbUseCase {
        return ObserveTaskBreadcrumbUseCase(taskRepository)
    }
}
