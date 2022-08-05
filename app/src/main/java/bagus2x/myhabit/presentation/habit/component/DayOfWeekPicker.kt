package bagus2x.myhabit.presentation.habit.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bagus2x.myhabit.presentation.common.component.coloredShadow
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme
import com.google.accompanist.flowlayout.FlowRow
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

private val weekdays = DayOfWeek.values().map { it.value }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DayOfWeekPicker(
    value: DayOfWeek,
    onChange: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        crossAxisSpacing = 8.dp,
        mainAxisSpacing = 8.dp
    ) {
        weekdays.forEach { dayOfWeekValue ->
            val isSelected = dayOfWeekValue == value.value
            FilterChip(
                selected = isSelected,
                onClick = { onChange(DayOfWeek.of(dayOfWeekValue)) },
                colors = ChipDefaults.filterChipColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.onSurface,
                    selectedContentColor = MaterialTheme.colors.surface,
                    selectedBackgroundColor = MaterialTheme.colors.primary,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                modifier = if (isSelected) Modifier.coloredShadow(
                    color = MaterialTheme.colors.primary,
                    alpha = 0.25F
                ) else Modifier
            ) {
                Text(
                    text = DayOfWeek
                        .of(dayOfWeekValue)
                        .getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun DayOfWeekPickerPreview() {
    MyHabitTheme {
        DayOfWeekPicker(
            value = LocalDateTime.now().dayOfWeek,
            onChange = {}
        )
    }
}
