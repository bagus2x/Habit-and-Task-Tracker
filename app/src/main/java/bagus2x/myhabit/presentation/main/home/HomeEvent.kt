package bagus2x.myhabit.presentation.main.home

sealed class HomeEvent {
    data class Snackbar(val message: String) : HomeEvent()
}
