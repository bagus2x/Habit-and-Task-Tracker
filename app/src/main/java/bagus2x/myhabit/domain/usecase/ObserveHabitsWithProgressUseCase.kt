package bagus2x.myhabit.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class ObserveHabitsWithProgressUseCase(
    private val habitRepository: HabitRepository
) {

    // Observe habit between dates
    operator fun invoke(date: LocalDateTime): Flow<PagingData<Habit.HabitWithProgress>> {
        val min = date.toLocalDate().atTime(0, 0)
        val max = date.toLocalDate().atTime(23, 59)
        return habitRepository.observeHabitsWithHistories(date.dayOfWeek)
            .map { pagingData ->
                pagingData.map { habitWithHistories ->
                    Habit.HabitWithProgress(
                        habit = habitWithHistories.habit,
                        progress = habitWithHistories.getTodayProgress(min, max)
                    )
                }
            }
    }

    private fun Habit.HabitWithHistories.getTodayProgress(
        min: LocalDateTime,
        max: LocalDateTime
    ): Float {
        val leastRemainingTimes = histories
            .filter { it.createdAt > min && it.createdAt < max }
            .minOfOrNull { it.remainingTimes } ?: return 0F
        return ((habit.focusTimes - leastRemainingTimes) / habit.focusTimes).toFloat()
    }
}
