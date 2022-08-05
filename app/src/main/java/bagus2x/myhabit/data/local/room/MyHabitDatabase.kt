package bagus2x.myhabit.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import bagus2x.myhabit.data.local.entity.HabitEntity
import bagus2x.myhabit.data.local.entity.HistoryEntity
import bagus2x.myhabit.data.local.entity.TaskEntity

@Database(
    version = 1,
    entities = [
        HabitEntity::class,
        HistoryEntity::class,
        TaskEntity::class
    ],
    exportSchema = false
)
abstract class MyHabitDatabase : RoomDatabase() {

    abstract val habitDao: HabitDao

    abstract val historyDao: HistoryDao

    abstract val taskDao: TaskDao
}
