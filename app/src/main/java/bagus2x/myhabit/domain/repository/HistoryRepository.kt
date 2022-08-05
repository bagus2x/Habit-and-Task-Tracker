package bagus2x.myhabit.domain.repository

import androidx.paging.PagingData
import bagus2x.myhabit.domain.model.History
import java.time.LocalDateTime
import kotlin.time.Duration

interface HistoryRepository {

    suspend fun create(habitId: Int, remainingTimes: Duration)

    suspend fun delete(historyId: Int)

//    fun observe(): PagingData<Pair<LocalDateTime, History>>
}
