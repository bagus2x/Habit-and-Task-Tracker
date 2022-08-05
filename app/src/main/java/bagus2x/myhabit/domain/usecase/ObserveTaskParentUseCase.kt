package bagus2x.myhabit.domain.usecase

import androidx.paging.PagingData
import bagus2x.myhabit.domain.model.Task
import bagus2x.myhabit.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ObserveTaskParentUseCase(
    private val taskRepository: TaskRepository
) {

    operator fun invoke(date: LocalDate): Flow<PagingData<Task>> {
        val min = date.atTime(0, 0)
        val max = date.atTime(23, 59)
        return taskRepository.observeParentBetweenDates(start = min, end = max)
    }
}
