package bagus2x.myhabit.domain.repository

import androidx.paging.PagingData
import bagus2x.myhabit.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TaskRepository {

    suspend fun create(
        parentTaskId: Long?,
        title: String,
        description: String,
        due: LocalDateTime?
    ): Long

    suspend fun getTask(taskId: Long): Task?

    fun observeTask(taskId: Long): Flow<Task?>

    fun observeParentBetweenDates(start: LocalDateTime, end: LocalDateTime): Flow<PagingData<Task>>

    fun observeHierarchy(hierarchy: Set<Long>): Flow<PagingData<Task>>

    suspend fun update(taskId: Long, title: String, description: String, due: LocalDateTime?)

    suspend fun delete(taskId: Long)
}
