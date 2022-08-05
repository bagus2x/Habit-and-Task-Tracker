package bagus2x.myhabit.presentation.habit.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bagus2x.myhabit.R
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.presentation.common.component.coloredShadow
import bagus2x.myhabit.presentation.common.theme.Green400
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme
import bagus2x.myhabit.presentation.common.theme.Orange400
import bagus2x.myhabit.presentation.common.theme.Red400

private val priorities = listOf(
    Habit.Priority.Low to Green400,
    Habit.Priority.Medium to Orange400,
    Habit.Priority.High to Red400
)

val Habit.Priority.text: String
    @Composable
    get() = when (this) {
        Habit.Priority.Low -> stringResource(R.string.text_low)
        Habit.Priority.Medium -> stringResource(R.string.text_medium)
        Habit.Priority.High -> stringResource(R.string.text_high)
    }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PriorityPicker(
    value: Habit.Priority,
    onChange: (Habit.Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        priorities.forEach { (priority, color) ->
            FilterChip(
                selected = priority == value,
                onClick = { onChange(priority) },
                colors = ChipDefaults.filterChipColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.onSurface,
                    selectedContentColor = MaterialTheme.colors.surface,
                    selectedBackgroundColor = color,
                ),
                border = BorderStroke(1.dp, color),
                modifier = if (priority == value) Modifier.coloredShadow(
                    color = color,
                    alpha = 0.25F
                ) else Modifier
            ) {
                Text(text = priority.text)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriorityPickerPreview() {
    MyHabitTheme {
        PriorityPicker(
            value = Habit.Priority.Low,
            onChange = { }
        )
    }
}
