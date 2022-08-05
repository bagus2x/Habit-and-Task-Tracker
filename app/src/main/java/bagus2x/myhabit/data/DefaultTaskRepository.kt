package bagus2x.myhabit.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import bagus2x.myhabit.data.local.TaskLocalDataSource
import bagus2x.myhabit.data.local.entity.TaskEntity
import bagus2x.myhabit.domain.model.Task
import bagus2x.myhabit.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneId

class DefaultTaskRepository(
    private val taskLocalDataSource: TaskLocalDataSource
) : TaskRepository {

    override suspend fun create(
        parentTaskId: Long?,
        title: String,
        description: String,
        due: LocalDateTime?,
    ): Long {
        return taskLocalDataSource.create(
            parentTaskId = parentTaskId,
            title = title,
            description = description,
            due = due?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        )
    }

    override suspend fun getTask(taskId: Long): Task? {
        return taskLocalDataSource.getTask(taskId)?.toModel()
    }

    override fun observeTask(taskId: Long): Flow<Task?> {
        return taskLocalDataSource.observeTask(taskId).map { it?.toModel() }
    }

    override fun observeParentBetweenDates(start: LocalDateTime, end: LocalDateTime): Flow<PagingData<Task>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                taskLocalDataSource.observeParent(
                    start = start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    end = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                )
            }
        )
        val pagingFlow = pager.flow
        return pagingFlow.map { it.map { entity -> entity.toModel() } }
    }

    override fun observeHierarchy(hierarchy: Set<Long>): Flow<PagingData<Task>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                taskLocalDataSource.observeHierarchy(TaskEntity.encodeHierarchy(hierarchy))
            }
        )
        val pagingFlow = pager.flow
        return pagingFlow.map { it.map { entity -> entity.toModel() } }
    }

    override suspend fun update(
        taskId: Long,
        title: String,
        description: String,
        due: LocalDateTime?
    ) {
        taskLocalDataSource.update(
            taskId = taskId,
            title = title,
            description = description,
            due = due?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        )
    }

    override suspend fun delete(taskId: Long) {
        taskLocalDataSource.delete(taskId)
    }
}
