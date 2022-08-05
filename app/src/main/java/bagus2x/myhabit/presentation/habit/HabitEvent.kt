package bagus2x.myhabit.presentation.habit

sealed class HabitEvent {
    data class Snackbar(val message: String) : HabitEvent()

    object PopBackStack : HabitEvent()
}
