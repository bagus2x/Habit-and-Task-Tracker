package bagus2x.myhabit.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "history",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["habit_id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistoryEntity(
    @ColumnInfo(name = "history_id")
    @PrimaryKey(autoGenerate = true)
    val historyId: Int = 0,
    @ColumnInfo(name = "habit_id", index = true)
    val habitId: Int,
    @ColumnInfo(name = "remaining_times")
    val remainingTimes: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
