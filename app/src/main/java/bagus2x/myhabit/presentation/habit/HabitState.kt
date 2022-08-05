package bagus2x.myhabit.presentation.habit

import androidx.compose.runtime.Immutable
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.model.History
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Immutable
data class HabitState(
    val habitId: Int = 0,
    val title: String,
    val description: String,
    val priority: Habit.Priority,
    val focusTime: Duration,
    val dayOfWeek: DayOfWeek,
    val histories: List<History>
) {
    val isModeEdit get() = habitId != 0

    companion object {
        val Default = HabitState(
            title = "",
            description = "",
            priority = Habit.Priority.Low,
            focusTime = 5.minutes,
            dayOfWeek = LocalDateTime.now().dayOfWeek,
            histories = emptyList()
        )
    }
}
