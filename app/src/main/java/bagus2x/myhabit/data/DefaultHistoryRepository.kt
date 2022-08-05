package bagus2x.myhabit.data

import bagus2x.myhabit.data.local.HistoryLocalDataSource
import bagus2x.myhabit.domain.repository.HistoryRepository
import kotlin.time.Duration

class DefaultHistoryRepository(
    private val historyLocalDataSource: HistoryLocalDataSource
) : HistoryRepository {

    override suspend fun create(habitId: Int, remainingTimes: Duration) {
        historyLocalDataSource.create(habitId, remainingTimes.inWholeMilliseconds)
    }

    override suspend fun delete(historyId: Int) {
        historyLocalDataSource.delete(historyId)
    }
}
