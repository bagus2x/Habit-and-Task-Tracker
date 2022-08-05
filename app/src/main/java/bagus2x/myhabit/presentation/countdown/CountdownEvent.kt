package bagus2x.myhabit.presentation.countdown

sealed class CountdownEvent {
    data class Snackbar(val message: String) : CountdownEvent()
}
