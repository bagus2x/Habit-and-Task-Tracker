package bagus2x.myhabit.domain.repository

import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {

    fun observe(): Flow<Boolean>
}
