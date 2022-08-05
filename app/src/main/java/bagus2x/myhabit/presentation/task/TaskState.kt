package bagus2x.myhabit.presentation.task

import androidx.compose.runtime.Immutable
import bagus2x.myhabit.domain.model.Task

@Immutable
data class TaskState(
    val parentTask: Task? = null,
    val isModeEdit: Boolean = false,
    val isChanged: Boolean = false,
    val breadcrumbs: List<Pair<Long, String>> = emptyList()
) {

    companion object {
        val Default = TaskState()
    }
}
