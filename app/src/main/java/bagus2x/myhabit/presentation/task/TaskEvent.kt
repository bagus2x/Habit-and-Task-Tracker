package bagus2x.myhabit.presentation.task

sealed class TaskEvent {
    data class Snackbar(val message: String) : TaskEvent()
    object PopBackStack : TaskEvent()
}
