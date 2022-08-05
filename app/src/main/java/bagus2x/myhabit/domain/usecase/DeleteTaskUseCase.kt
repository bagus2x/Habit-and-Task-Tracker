package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(taskId: Long) = runCatching {
        taskRepository.delete(taskId)
    }
}
