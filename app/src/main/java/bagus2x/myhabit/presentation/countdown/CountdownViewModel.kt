package bagus2x.myhabit.presentation.countdown

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bagus2x.myhabit.domain.usecase.CreateHistoryUseCase
import bagus2x.myhabit.domain.usecase.ObserveHabitWithHistoriesUseCase
import bagus2x.myhabit.presentation.common.BaseViewModel
import bagus2x.myhabit.presentation.main.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CountDownViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dispatcher: CoroutineDispatcher,
    private val observeHabitWithHistoriesUseCase: ObserveHabitWithHistoriesUseCase,
    private val createHistoryUseCase: CreateHistoryUseCase
) : BaseViewModel<CountdownState, CountdownEvent>(CountdownState.Default) {
    private var timerJob: Job? = null
    private val timer = uiState.timerFlow(onFinish = ::stop)

    init {
        loadHabit()
    }

    private fun loadHabit() {
        val habitId = savedStateHandle.get<Int>(Destination.KEY_HABIT_ID) ?: return
        viewModelScope.launch(dispatcher) {
            observeHabitWithHistoriesUseCase(habitId).collectLatest { habitWithHistories ->
                updateUiState {
                    it.copy(
                        habit = habitWithHistories.habit,
                        timesRemaining = habitWithHistories.habit.focusTimes,
                        histories = habitWithHistories.histories
                    )
                }
            }
        }
    }

    fun start() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            timer.collectLatest { remaining ->
                updateUiState {
                    it.copy(
                        timesRemaining = remaining,
                        isRunning = true
                    )
                }
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
        val habit = uiState.value.habit ?: return
        val remaining = uiState.value.timesRemaining
        viewModelScope.launch(dispatcher) {
            createHistoryUseCase(
                habitId = habit.habitId,
                remainingTimes = remaining
            ).onFailure { e ->
                sendUiEvent(CountdownEvent.Snackbar(e.message ?: "Failed to record history"))
            }
        }
        updateUiState {
            it.copy(
                isRunning = false,
                timesRemaining = habit.focusTimes
            )
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun Flow<CountdownState>.timerFlow(
    interval: Duration = 1.seconds,
    onFinish: () -> Unit
): Flow<Duration> {
    return this
        .distinctUntilChanged { old, new -> old.habit == new.habit }
        .filterNot { it.habit == null }
        .flatMapLatest { state ->
            timerFlow(
                duration = state.timesRemaining,
                interval = interval,
                onFinish = onFinish
            )
        }
}

private fun timerFlow(
    duration: Duration,
    interval: Duration,
    onFinish: () -> Unit
): Flow<Duration> = callbackFlow {
    var remaining = duration
    while (remaining >= Duration.ZERO) {
        send(remaining)
        remaining -= interval
        delay(interval.inWholeMilliseconds)
    }
    onFinish()
    cancel()
}
