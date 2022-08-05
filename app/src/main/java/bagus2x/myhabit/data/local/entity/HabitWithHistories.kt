package bagus2x.myhabit.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithHistories(
    @Embedded
    val habit: HabitEntity,
    @Relation(
        parentColumn = "habit_id",
        entityColumn = "habit_id"
    )
    val histories: List<HistoryEntity>
)
