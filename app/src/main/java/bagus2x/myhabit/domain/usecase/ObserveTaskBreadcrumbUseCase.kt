package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class ObserveTaskBreadcrumbUseCase(
    private val taskRepository: TaskRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(taskId: Long): Flow<Set<Pair<Long, String>>> {
        return taskRepository.observeTask(taskId)
            .filterNotNull()
            .distinctUntilChanged { old, new -> old.taskId == new.taskId }
            .flatMapLatest { task ->
                val tasksFlow = task.hierarchy.map { taskRepository.observeTask(it).filterNotNull() }
                combine(tasksFlow) { tasks -> tasks.map { it.taskId to it.title }.toSet() }
            }
    }
}
