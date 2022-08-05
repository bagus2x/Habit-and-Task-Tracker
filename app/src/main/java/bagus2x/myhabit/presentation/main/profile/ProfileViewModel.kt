package bagus2x.myhabit.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.myhabit.domain.repository.ConnectivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val observableConnectivity: ConnectivityRepository
) : ViewModel() {
    val connectivityManager =
        observableConnectivity.observe().stateIn(viewModelScope, SharingStarted.Lazily, null)
}
