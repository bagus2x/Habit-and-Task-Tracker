package bagus2x.myhabit.domain.usecase

import androidx.paging.PagingData
import bagus2x.myhabit.domain.model.Task
import bagus2x.myhabit.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class ObserveTaskHierarchyUseCase(
    private val taskRepository: TaskRepository
) {

    operator fun invoke(hierarchy: Set<Long>): Flow<PagingData<Task>> {
        return taskRepository.observeHierarchy(hierarchy)
    }
}
