package bagus2x.myhabit.data.local

import androidx.paging.PagingSource
import bagus2x.myhabit.data.local.entity.TaskEntity
import bagus2x.myhabit.data.local.room.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskLocalDataSource(
    private val taskDao: TaskDao
) {

    suspend fun create(parentTaskId: Long?, title: String, description: String, due: Long?): Long {
        return taskDao.create(parentTaskId, title, description, due)
    }

    suspend fun getTask(taskId: Long): TaskEntity? {
        return taskDao.getTask(taskId)
    }

    fun observeTask(taskId: Long): Flow<TaskEntity?> {
        return taskDao.observe(taskId)
    }

    fun observeParent(start: Long, end: Long): PagingSource<Int, TaskEntity> {
        return taskDao.observeParents(start, end)
    }

    fun observeHierarchy(hierarchy: String): PagingSource<Int, TaskEntity> {
        return taskDao.observeHierarchy(hierarchy)
    }

    suspend fun update(taskId: Long, title: String, description: String, due: Long?) {
        taskDao.update(taskId, title, description, due)
    }

    suspend fun delete(taskId: Long) {
        taskDao.delete(taskId)
    }
}
