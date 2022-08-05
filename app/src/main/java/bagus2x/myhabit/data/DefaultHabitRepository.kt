package bagus2x.myhabit.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import bagus2x.myhabit.data.local.HabitLocalDataSource
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import kotlin.time.Duration

class DefaultHabitRepository(
    private val habitLocalDataSource: HabitLocalDataSource
) : HabitRepository {

    override suspend fun create(
        title: String,
        description: String,
        priority: Habit.Priority,
        focusTime: Duration,
        dayOfWeek: DayOfWeek
    ) {
        habitLocalDataSource.create(
            title = title,
            description = description,
            priority = priority.name,
            focusTime = focusTime.inWholeMilliseconds,
            dayOfWeek = dayOfWeek.value
        )
    }

    override suspend fun update(
        habitId: Int,
        title: String,
        description: String,
        priority: Habit.Priority,
        focusTime: Duration,
        dayOfWeek: DayOfWeek
    ) {
        habitLocalDataSource.update(
            habitId = habitId,
            title = title,
            description = description,
            priority = priority.name,
            focusTime = focusTime.inWholeMilliseconds,
            dayOfWeek = dayOfWeek.value
        )
    }

    override suspend fun getHabit(habitId: Int): Habit? {
        return habitLocalDataSource.getHabit(habitId)?.toModel()
    }

    override fun observeHabitWithHistories(habitId: Int): Flow<Habit.HabitWithHistories> {
        return habitLocalDataSource.observeHabitAndHistories(habitId).map { it.toModel() }
    }

    override fun observeHabitsWithHistories(dayOfWeek: DayOfWeek): Flow<PagingData<Habit.HabitWithHistories>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { habitLocalDataSource.observeHabitsAndHistories(dayOfWeek.value) }
        )
        val pagingFlow = pager.flow
        return pagingFlow.map { it.map { entity -> entity.toModel() } }
    }

    override suspend fun delete(habitId: Int) {
        habitLocalDataSource.delete(habitId)
    }
}
