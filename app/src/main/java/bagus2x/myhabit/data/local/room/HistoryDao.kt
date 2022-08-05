package bagus2x.myhabit.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import bagus2x.myhabit.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {

    @Insert
    suspend fun create(historyEntity: HistoryEntity)

    @Query("DELETE FROM history WHERE history_id = :historyId")
    suspend fun delete(historyId: Int)
}
