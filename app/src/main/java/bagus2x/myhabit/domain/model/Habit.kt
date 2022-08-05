package bagus2x.myhabit.domain.model

import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.time.Duration

data class Habit(
    val habitId: Int,
    val title: String,
    val description: String,
    val priority: Priority,
    val focusTimes: Duration,
    val dayOfWeek: DayOfWeek,
    val createdAt: LocalDateTime
) {

    enum class Priority { Low, Medium, High }

    data class HabitWithHistories(
        val habit: Habit,
        val histories: List<History>
    )

    data class HabitWithProgress(
        val habit: Habit,
        val progress: Float
    )
}
