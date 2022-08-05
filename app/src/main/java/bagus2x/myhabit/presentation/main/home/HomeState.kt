package bagus2x.myhabit.presentation.main.home

import androidx.compose.runtime.Immutable
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@Immutable
data class HomeState(
    val username: String,
    val selectedDate: LocalDateTime?,
    val firstDayOfWeek: LocalDateTime = LocalDateTime.now()
        .with(LocalTime.MIN)
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
) {
    companion object {
        val Default = HomeState(
            username = "Username",
            selectedDate = LocalDateTime.now()
        )
    }
}
