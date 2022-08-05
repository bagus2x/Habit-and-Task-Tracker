package bagus2x.myhabit.data.local

import bagus2x.myhabit.data.local.entity.HistoryEntity
import bagus2x.myhabit.data.local.room.HistoryDao

class HistoryLocalDataSource(
    private val historyDao: HistoryDao
) {

    suspend fun create(habitId: Int, remainingTimes: Long) {
        val history = HistoryEntity(
            habitId = habitId,
            remainingTimes = remainingTimes
        )
        historyDao.create(history)
    }

    suspend fun delete(historyId: Int) {
        historyDao.delete(historyId)
    }
}
