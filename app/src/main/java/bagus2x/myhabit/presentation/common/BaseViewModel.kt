package bagus2x.myhabit.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, E>(initialState: S) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<E>()
    val uiEvent = _uiEvent
        .receiveAsFlow()
        .shareIn(viewModelScope, SharingStarted.Lazily)

    protected fun updateUiState(function: (S) -> S) {
        _uiState.update(function)
    }

    protected fun sendUiEvent(event: E) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
