package bagus2x.myhabit.presentation.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import bagus2x.myhabit.domain.model.Task
import bagus2x.myhabit.domain.usecase.*
import bagus2x.myhabit.presentation.common.BaseViewModel
import bagus2x.myhabit.presentation.main.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val dispatcher: CoroutineDispatcher,
    private val workManager: WorkManager,
    private val createTaskUseCase: CreateTaskUseCase,
    private val observeTaskBreadcrumbUseCase: ObserveTaskBreadcrumbUseCase,
    private val observeTaskHierarchyUseCase: ObserveTaskHierarchyUseCase,
    private val observeTaskUseCase: ObserveTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : BaseViewModel<TaskState, TaskEvent>(TaskState.Default) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val childrenTask = uiState
        .map { it.parentTask }
        .filterNotNull()
        .flatMapLatest { parentTask ->
            observeTaskHierarchyUseCase(parentTask.hierarchy)
                .map { pagingData ->
                    pagingData
                        .filter { it.taskId != parentTask.taskId }
                        .map { it to getIndent(it.hierarchy, parentTask.hierarchy) }
                }
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())

    init {
        loadParentTask()
    }

    private fun getIndent(childHierarchy: Set<Long>, parentHierarchy: Set<Long>): Int {
        return (childHierarchy - parentHierarchy).size - 1
    }

    private fun loadParentTask() {
        viewModelScope.launch(dispatcher) {
            var parentTaskId = savedState.get<Long>(Destination.KEY_TASK_ID).takeIf { it != 0L }
            updateUiState { it.copy(isModeEdit = parentTaskId != null) }
            parentTaskId = parentTaskId ?: createTask()
                .onFailure { e ->
                    Timber.e(e)
                    sendUiEvent(
                        TaskEvent.Snackbar(
                            e.message ?: "Cannot create parent task"
                        )
                    )
                }.getOrNull()
            if (parentTaskId == null) {
                return@launch
            }
            launch {
                observeTaskBreadcrumbUseCase(parentTaskId).collect { breadcrumbs ->
                    updateUiState { it.copy(breadcrumbs = breadcrumbs.toList()) }
                }
            }
            launch {
                observeTaskUseCase(parentTaskId).collectLatest { task ->
                    updateUiState { it.copy(parentTask = task) }
                }
            }
        }
    }

    private suspend fun createTask(
        parentTaskId: Long? = null,
        title: String = "",
        description: String = "",
        due: LocalDateTime? = null
    ): Result<Long> {
        return createTaskUseCase(parentTaskId, title, description, due)
    }

    fun createChildTask(parentTaskId: Long) {
        viewModelScope.launch(dispatcher) {
            createTask(parentTaskId)
                .onFailure {
                    sendUiEvent(TaskEvent.Snackbar(it.message ?: "Cannot save changes"))
                }
                .onSuccess {
                    uiState.value.parentTask?.let(::notify)
                }
        }
    }

    fun changeTitle(title: String) {
        updateUiState { state ->
            state.parentTask?.let {
                state.copy(
                    parentTask = it.copy(title = title),
                    isChanged = true
                )
            } ?: state
        }
    }

    fun changeDescription(description: String) {
        updateUiState { state ->
            state.parentTask?.let {
                state.copy(
                    parentTask = it.copy(description = description),
                    isChanged = true
                )
            } ?: state
        }
    }

    fun changeDate(localDate: LocalDate) {
        updateUiState { state ->
            if (state.parentTask?.due == null) {
                return@updateUiState state
            }
            state.copy(
                parentTask = state.parentTask.copy(
                    due = LocalDateTime.of(localDate, state.parentTask.due.toLocalTime())
                ),
                isChanged = true
            )
        }
    }

    fun changeTime(localTime: LocalTime) {
        updateUiState { state ->
            if (state.parentTask?.due == null) {
                return@updateUiState state
            }
            state.copy(
                parentTask = state.parentTask.copy(
                    due = LocalDateTime.of(state.parentTask.due.toLocalDate(), localTime)
                ),
                isChanged = true
            )
        }
    }

    fun saveUpdatedTask() {
        viewModelScope.launch(dispatcher) {
            val parentTask = uiState.value.parentTask ?: return@launch
            updateTaskUseCase(
                taskId = parentTask.taskId,
                title = parentTask.title,
                description = parentTask.description,
                due = parentTask.due
            )
                .onFailure {
                    sendUiEvent(TaskEvent.Snackbar(it.message ?: "Cannot save changes"))
                }
                .onSuccess {
                    updateUiState { it.copy(isChanged = false) }
                    uiState.value.parentTask?.let(::notify)
                }
        }
    }

    private fun notify(task: Task) {
        if (task.due == null || task.due < LocalDateTime.now()) {
            return
        }
        val delay = task.due - LocalDateTime.now()
        val data = Data.Builder()
            .putLong(TaskNotificationWorker.KEY_TASK_ID, task.taskId)
            .build()
        val request = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        val workId = task.taskId.toString()
        workManager.enqueueUniqueWork(workId, ExistingWorkPolicy.REPLACE, request)
    }

    fun deleteTask(taskId: Long) {
        val parentTaskId = uiState.value.parentTask?.taskId
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
                .onSuccess {
                    // Pop back stack if parent task was deleted
                    if (parentTaskId == taskId) {
                        sendUiEvent(TaskEvent.PopBackStack)
                    }
                }
                .onFailure { e ->
                    Timber.e(e)
                    sendUiEvent(TaskEvent.Snackbar(e.message ?: "Cannot delete this task"))
                }
        }
    }
}

private operator fun LocalDateTime.minus(other: LocalDateTime): Long {
    return with(ZoneId.systemDefault()) {
        this@minus.atZone(this).toInstant().toEpochMilli() - other.atZone(this)
            .toInstant().toEpochMilli()
    }
}
