package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.repository.HabitRepository

class DeleteHabitUseCase(
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(habitId: Int) = runCatching {
        habitRepository.delete(habitId)
    }
}
