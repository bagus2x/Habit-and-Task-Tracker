package bagus2x.myhabit.presentation.main.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import bagus2x.myhabit.domain.usecase.DeleteHabitUseCase
import bagus2x.myhabit.domain.usecase.ObserveHabitsWithProgressUseCase
import bagus2x.myhabit.domain.usecase.ObserveTaskParentUseCase
import bagus2x.myhabit.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val observeHabitsWithProgressUseCase: ObserveHabitsWithProgressUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val observeTaskParentUseCase: ObserveTaskParentUseCase
) : BaseViewModel<HomeState, HomeEvent>(HomeState.Default) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val habits = uiState
        .distinctUntilChanged { old, new -> old.selectedDate?.dayOfWeek == new.selectedDate?.dayOfWeek }
        .flatMapLatest { homeState ->
            val date = homeState.selectedDate ?: LocalDateTime.now()
            observeHabitsWithProgressUseCase(date)
        }
        .catch { e ->
            Timber.e(e)
            sendUiEvent(HomeEvent.Snackbar(e.message ?: "Failed to load habits"))
        }
        .flowOn(dispatcher)
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks = uiState
        .distinctUntilChanged { old, new -> old.selectedDate == new.selectedDate }
        .flatMapLatest { homeState ->
            val date = homeState.selectedDate ?: LocalDateTime.now()
            observeTaskParentUseCase(date.toLocalDate())
        }
        .flowOn(dispatcher)
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    fun selectLocalDateTime(localDateTime: LocalDateTime?) {
        updateUiState { it.copy(selectedDate = localDateTime) }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch(dispatcher) { deleteHabitUseCase(habitId) }
    }
}
