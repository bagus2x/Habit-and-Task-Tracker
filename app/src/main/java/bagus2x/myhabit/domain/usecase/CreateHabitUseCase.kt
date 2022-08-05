package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.repository.HabitRepository
import java.time.DayOfWeek
import kotlin.time.Duration

class CreateHabitUseCase(
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(
        title: String,
        description: String,
        priority: Habit.Priority,
        focusTime: Duration,
        dayOfWeek: DayOfWeek
    ) = runCatching {
        habitRepository.create(title, description, priority, focusTime, dayOfWeek)
    }
}
