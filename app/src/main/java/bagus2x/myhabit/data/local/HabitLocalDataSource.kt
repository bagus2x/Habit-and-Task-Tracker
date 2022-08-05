package bagus2x.myhabit.data.local

import androidx.paging.PagingSource
import bagus2x.myhabit.data.local.entity.HabitEntity
import bagus2x.myhabit.data.local.entity.HabitWithHistories
import bagus2x.myhabit.data.local.room.HabitDao
import kotlinx.coroutines.flow.Flow

class HabitLocalDataSource(
    private val habitDao: HabitDao
) {

    suspend fun create(
        title: String,
        description: String,
        priority: String,
        focusTime: Long,
        dayOfWeek: Int
    ) {
        val habit = HabitEntity(
            title = title,
            description = description,
            priority = priority,
            dayOfWeek = dayOfWeek,
            focusTimes = focusTime
        )
        habitDao.create(habit)
    }

    suspend fun getHabit(habitId: Int): HabitEntity? {
        return habitDao.getHabit(habitId)
    }

    fun observeHabitAndHistories(habitId: Int): Flow<HabitWithHistories> {
        return habitDao.observeHabitWithHistories(habitId)
    }

    fun observeHabitsAndHistories(dayOfWeek: Int): PagingSource<Int, HabitWithHistories> {
        return habitDao.observeHabitsWithHistories(dayOfWeek)
    }

    suspend fun update(
        habitId: Int,
        title: String,
        description: String,
        priority: String,
        focusTime: Long,
        dayOfWeek: Int
    ) {
        val habit = habitDao.getHabit(habitId)?.copy(
            title = title,
            description = description,
            priority = priority,
            dayOfWeek = dayOfWeek,
            focusTimes = focusTime,
        )
        requireNotNull(habit)
        habitDao.update(habit)
    }

    suspend fun delete(habitId: Int) {
        habitDao.delete(habitId)
    }
}
