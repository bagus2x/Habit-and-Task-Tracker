package bagus2x.myhabit.presentation.task.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun TaskTopBar(
    modifier: Modifier = Modifier,
    onClickButtonBack: () -> Unit,
    dateTime: LocalDateTime?,
    onChangeDate: (LocalDate) -> Unit,
    onChangeTime: (LocalTime) -> Unit,
    savable: Boolean,
    onClickButtonSave: () -> Unit,
    onClickMenuAddChild: () -> Unit,
    onClickMenuDelete: () -> Unit
) {
    val dialogDatePickerState = rememberMaterialDialogState()
    val dialogTimePickerState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogDatePickerState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker(
            initialDate = dateTime?.toLocalDate() ?: LocalDate.now(),
            onDateChange = onChangeDate
        )
    }
    MaterialDialog(
        dialogState = dialogTimePickerState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker(
            initialTime = dateTime?.toLocalTime() ?: LocalTime.now(),
            onTimeChange = onChangeTime
        )
    }
    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = 0.dp,
        contentPadding = WindowInsets.statusBars.asPaddingValues(LocalDensity.current)
    ) {
        IconButton(onClick = onClickButtonBack) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1F)
        ) {
            if (dateTime != null) {
                val (dateFormat, timeFormat) = rememberFormatter()
                val (date, time) = remember(dateTime) {
                    with(dateTime) { format(dateFormat) to format(timeFormat) }
                }
                Text(
                    text = date,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(onClick = { dialogDatePickerState.show() })
                )
                Text(
                    text = time,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(onClick = { dialogTimePickerState.show() })
                )
            }
        }
        TopBarMenu(
            onClickMenuAddChild = onClickMenuAddChild,
            onClickMenuDelete = onClickMenuDelete
        )
        IconButton(
            onClick = onClickButtonSave,
            enabled = savable
        ) {
            Icon(
                imageVector = Icons.Outlined.Done,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun rememberFormatter(): Pair<DateTimeFormatter, DateTimeFormatter> {
    return remember {
        DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault()) to
                DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
    }
}
