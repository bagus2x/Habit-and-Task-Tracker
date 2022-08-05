package bagus2x.myhabit.data.local.room

import androidx.paging.PagingSource
import androidx.room.*
import bagus2x.myhabit.data.local.entity.HabitEntity
import bagus2x.myhabit.data.local.entity.HabitWithHistories
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert
    suspend fun create(habit: HabitEntity)

    @Query("SELECT * FROM habit WHERE habit_id = :habitId")
    suspend fun getHabit(habitId: Int): HabitEntity?

    @Update
    suspend fun update(habit: HabitEntity)

    @Transaction
    @Query("SELECT * FROM habit WHERE habit_id = :habitId")
    fun observeHabitWithHistories(habitId: Int): Flow<HabitWithHistories>

    @Transaction
    @Query("SELECT * FROM habit WHERE day_of_week = :dayOfWeek ORDER By created_at DESC")
    fun observeHabitsWithHistories(dayOfWeek: Int): PagingSource<Int, HabitWithHistories>

    @Query("DELETE FROM habit WHERE habit_id = :habitId")
    suspend fun delete(habitId: Int)
}
