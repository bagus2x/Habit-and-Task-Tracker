package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.repository.HabitRepository
import java.time.DayOfWeek
import kotlin.time.Duration

class UpdateHabitUseCase(
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(
        habitId: Int,
        title: String,
        description: String,
        priority: Habit.Priority,
        focusTime: Duration,
        dayOfWeek: DayOfWeek
    ) = runCatching {
        habitRepository.update(habitId, title, description, priority, focusTime, dayOfWeek)
    }
}
