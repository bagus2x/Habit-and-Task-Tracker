package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.repository.TaskRepository
import java.time.LocalDateTime

class UpdateTaskUseCase(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(
        taskId: Long,
        title: String,
        description: String,
        due: LocalDateTime?
    ) = runCatching {
        taskRepository.update(taskId, title, description, due)
    }
}
