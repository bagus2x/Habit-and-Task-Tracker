package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow

class ObserveHabitWithHistoriesUseCase(
    private val habitRepository: HabitRepository
) {

    operator fun invoke(habitId: Int): Flow<Habit.HabitWithHistories> {
        return habitRepository.observeHabitWithHistories(habitId)
    }
}
