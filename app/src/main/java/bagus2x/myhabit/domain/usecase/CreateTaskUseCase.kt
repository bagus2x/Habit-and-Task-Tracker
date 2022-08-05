package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.repository.TaskRepository
import java.time.LocalDateTime

class CreateTaskUseCase(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(
        parentTaskId: Long?,
        title: String,
        description: String,
        due: LocalDateTime?,
    ) = runCatching {
        val newDue = if (parentTaskId == null) due ?: LocalDateTime.now() else null
        taskRepository.create(
            parentTaskId = parentTaskId,
            title = title,
            description = description,
            due = newDue
        )
    }
}
