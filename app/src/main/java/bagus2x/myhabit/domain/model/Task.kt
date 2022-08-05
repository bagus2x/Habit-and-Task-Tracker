package bagus2x.myhabit.domain.model

import java.time.LocalDateTime

data class Task(
    val taskId: Long,
    val parentTaskId: Long?,
    val title: String,
    val description: String,
    val due: LocalDateTime?,
    val createdAt: LocalDateTime,
    val hierarchy: Set<Long>
)
