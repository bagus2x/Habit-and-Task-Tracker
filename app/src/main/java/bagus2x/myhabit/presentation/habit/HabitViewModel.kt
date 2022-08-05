package bagus2x.myhabit.presentation.habit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.usecase.CreateHabitUseCase
import bagus2x.myhabit.domain.usecase.ObserveHabitWithHistoriesUseCase
import bagus2x.myhabit.domain.usecase.UpdateHabitUseCase
import bagus2x.myhabit.presentation.common.BaseViewModel
import bagus2x.myhabit.presentation.main.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class HabitViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val observeHabitWithHistoriesUseCase: ObserveHabitWithHistoriesUseCase,
    private val createHabitUseCase: CreateHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel<HabitState, HabitEvent>(
    HabitState.Default.copy(
        habitId = savedState[Destination.KEY_HABIT_ID] ?: 0
    )
) {
    init {
        savedState.get<Int>(Destination.KEY_HABIT_ID)?.let(::loadHabit)
    }

    private fun loadHabit(habitId: Int) {
        if (habitId == 0) {
            return
        }

        viewModelScope.launch(dispatcher) {
            observeHabitWithHistoriesUseCase.invoke(habitId).collectLatest { habitWithHistories ->
                val habit = habitWithHistories.habit
                val histories = habitWithHistories.histories
                updateUiState {
                    it.copy(
                        title = habit.title,
                        description = habit.description,
                        priority = habit.priority,
                        focusTime = habit.focusTimes,
                        dayOfWeek = habit.dayOfWeek,
                        histories = histories
                    )
                }
            }
        }
    }

    fun changeTitle(title: String) {
        updateUiState { it.copy(title = title) }
    }

    fun changeDescription(description: String) {
        updateUiState { it.copy(description = description) }
    }

    fun changePriority(priority: Habit.Priority) {
        updateUiState { it.copy(priority = priority) }
    }

    fun changeFocusTime(focusTime: Duration) {
        updateUiState { it.copy(focusTime = focusTime) }
    }

    fun changeDayOfWeek(dayOfWeek: DayOfWeek) {
        updateUiState { it.copy(dayOfWeek = dayOfWeek) }
    }

    fun createOrUpdate() {
        viewModelScope.launch(dispatcher) {
            uiState.value.run {
                if (habitId == 0) {
                    createHabitUseCase(title, description, priority, focusTime, dayOfWeek)
                        .onSuccess {
                            sendUiEvent(HabitEvent.PopBackStack)
                        }
                        .onFailure { e ->
                            sendUiEvent(HabitEvent.Snackbar(e.message ?: "Failed to create habit"))
                        }
                } else {
                    updateHabitUseCase(habitId, title, description, priority, focusTime, dayOfWeek)
                        .onSuccess {
                            sendUiEvent(HabitEvent.PopBackStack)
                        }
                        .onFailure { e ->
                            sendUiEvent(HabitEvent.Snackbar(e.message ?: "Failed to create habit"))
                        }
                }
            }
        }
    }

    fun popBackStack() {
        sendUiEvent(HabitEvent.PopBackStack)
    }
}
