package bagus2x.myhabit.domain.usecase

import bagus2x.myhabit.domain.repository.HistoryRepository

class DeleteHistoryUseCase(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(historyId: Int) = runCatching {
        historyRepository.delete(historyId)
    }
}
