package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.model.Task
import bagus2x.myhabit.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class ObserveTaskUseCase(
    private val taskRepository: TaskRepository
) {

    operator fun invoke(taskId: Long): Flow<Task?> {
        return taskRepository.observeTask(taskId)
    }
}
