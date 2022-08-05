package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.repository.HabitRepository
import bagus2x.myhabit.domain.repository.HistoryRepository
import java.time.LocalDateTime
import kotlin.time.Duration

class CreateHistoryUseCase(
    private val habitRepository: HabitRepository,
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(
        habitId: Int,
        remainingTimes: Duration,
        createdAt: LocalDateTime = LocalDateTime.now()
    ) = runCatching {
        val habit = habitRepository.getHabit(habitId)
        requireNotNull(habit)
        val matches = with(createdAt) { dayOfWeek != habit.dayOfWeek }
        if (matches) {
            throw IllegalArgumentException("The day of week does not match")
        }
        historyRepository.create(habitId, remainingTimes)
    }
}
