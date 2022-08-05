package bagus2x.myhabit.presentation.countdown

import androidx.compose.runtime.Immutable
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.model.History
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Immutable
data class CountdownState(
    val habit: Habit?,
    val histories: List<History>,
    val timesRemaining: Duration,
    val isRunning: Boolean
) {

    companion object {
        val Default = CountdownState(
            habit = null,
            histories = emptyList(),
            timesRemaining = 0.milliseconds,
            isRunning = false
        )
    }
}
