package bagus2x.myhabit.data.local.room

import androidx.paging.PagingSource
import androidx.room.*
import bagus2x.myhabit.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun create(task: TaskEntity): Long

    @Transaction
    suspend fun create(
        parentTaskId: Long?,
        title: String,
        description: String,
        due: Long?,
    ): Long {
        val parent = parentTaskId?.let { getTask(it) }
        val task = TaskEntity(
            parentTaskId = parentTaskId,
            title = title,
            description = description,
            due = due,
            hierarchy = ""
        )
        val taskId = create(task)
        val newHierarchy = parent?.hierarchy?.let {
            TaskEntity.decodeHierarchy(it)
                .toMutableSet()
                .apply { add(taskId) }
                .toSet()
        } ?: setOf(taskId)
        updateHierarchy(
            taskId = taskId,
            hierarchy = TaskEntity.encodeHierarchy(newHierarchy)
        )
        return taskId
    }

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Transaction
    suspend fun update(taskId: Long, title: String, description: String, due: Long?) {
        val task = getTask(taskId)
        requireNotNull(task)
        update(
            taskEntity = task.copy(
                title = title,
                description = description,
                due = due
            )
        )
    }

    @Query("UPDATE task SET hierarchy = :hierarchy WHERE task_id = :taskId")
    suspend fun updateHierarchy(taskId: Long, hierarchy: String)

    @Query("SELECT * FROM task WHERE task_id = :taskId")
    suspend fun getTask(taskId: Long): TaskEntity?

    @Query("SELECT * FROM task WHERE task_id = :taskId")
    fun observe(taskId: Long): Flow<TaskEntity?>

    @Query("SELECT * FROM task WHERE parent_task_id IS NULL AND due BETWEEN :start AND :end")
    fun observeParents(start: Long, end: Long): PagingSource<Int, TaskEntity>

    @Query("SELECT * FROM task WHERE hierarchy LIKE :hierarchy || '%' ORDER BY hierarchy")
    fun observeHierarchy(hierarchy: String): PagingSource<Int, TaskEntity>

    @Query("DELETE FROM task WHERE task_id = :taskId")
    suspend fun delete(taskId: Long)
}
