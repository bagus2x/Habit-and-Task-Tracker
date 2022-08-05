package bagus2x.myhabit.domain.model

import java.time.LocalDateTime
import kotlin.time.Duration

data class History(
    val historyId: Int,
    val remainingTimes: Duration,
    val createdAt: LocalDateTime
)
