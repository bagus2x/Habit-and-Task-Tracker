package bagus2x.myhabit.presentation.habit.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bagus2x.myhabit.R
import bagus2x.myhabit.presentation.common.component.coloredShadow
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme
import com.google.accompanist.flowlayout.FlowRow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

private val minutes = listOf(
    5.minutes,
    10.minutes,
    20.minutes,
    30.minutes,
    40.minutes,
    1.hours,
    2.hours
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FocusTimePicker(
    value: Duration,
    onChange: (Duration) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        crossAxisSpacing = 8.dp,
        mainAxisSpacing = 8.dp
    ) {
        minutes.forEach { duration ->
            val isSelected = duration == value
            FilterChip(
                selected = isSelected,
                onClick = { onChange(duration) },
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
                    text = duration.text,
                    modifier = Modifier.widthIn(24.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private val Duration.text: String
    @Composable
    get() = toComponents { hours, minutes, _, _ ->
        return@toComponents when {
            hours != 0L -> stringResource(R.string.text_hours, hours)
            minutes != 0 -> stringResource(R.string.text_minutes, minutes)
            else -> ""
        }
    }

@Preview
@Composable
fun FocusTimePickerPreview() {
    MyHabitTheme {
        FocusTimePicker(
            value = 5.minutes,
            onChange = {}
        )
    }
}
