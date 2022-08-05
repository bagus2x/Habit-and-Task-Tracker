package bagus2x.myhabit.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class HabitEntity constructor(
    @ColumnInfo(name = "habit_id")
    @PrimaryKey(autoGenerate = true)
    val habitId: Int = 0,
    val title: String,
    val description: String,
    val priority: String,
    @ColumnInfo(name = "focus_times")
    val focusTimes: Long,
    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)
