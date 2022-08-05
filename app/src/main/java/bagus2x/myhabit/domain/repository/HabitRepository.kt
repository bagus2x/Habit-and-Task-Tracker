package bagus2x.myhabit.domain.repository

import androidx.paging.PagingData
import bagus2x.myhabit.domain.model.Habit
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import kotlin.time.Duration

interface HabitRepository {

    suspend fun create(
        title: String,
        description: String,
        priority: Habit.Priority,
        focusTime: Duration,
        dayOfWeek: DayOfWeek
    )

    suspend fun update(
        habitId: Int,
        title: String,
        description: String,
        priority: Habit.Priority,
        focusTime: Duration,
        dayOfWeek: DayOfWeek
    )

    suspend fun getHabit(habitId: Int): Habit?

    fun observeHabitWithHistories(habitId: Int): Flow<Habit.HabitWithHistories>

    fun observeHabitsWithHistories(dayOfWeek: DayOfWeek): Flow<PagingData<Habit.HabitWithHistories>>

    suspend fun delete(habitId: Int)
}
