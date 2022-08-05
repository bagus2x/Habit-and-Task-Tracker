package bagus2x.myhabit.presentation.main.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.presentation.common.component.ProgressIndicator
import bagus2x.myhabit.presentation.common.theme.Green400
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme
import bagus2x.myhabit.presentation.common.theme.Orange400
import bagus2x.myhabit.presentation.common.theme.Red400
import java.text.DecimalFormat
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

private val DecimalFormat = DecimalFormat("0.#")

@Composable
fun HabitCard(
    title: String,
    progress: Float,
    priority: Habit.Priority,
    focusTimes: Duration,
    onClickCard: () -> Unit,
    onClickDetail: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = 0.dp,
        backgroundColor = priority.color,
        contentColor = MaterialTheme.colors.surface
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clickable(onClick = onClickCard)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
                Text(
                    text = focusTimes.toString(DurationUnit.MINUTES),
                    style = MaterialTheme.typography.overline
                )
                HabitCardDropdown(
                    expanded = expanded,
                    onClick = { expanded = true },
                    onDismissRequest = { expanded = false },
                    onClickDetail = {
                        expanded = false
                        onClickDetail()
                    },
                    onClickDelete = {
                        expanded = false
                        onClickDelete()
                    }
                )
            }
            Box {
                ProgressIndicator(
                    progressProvider = { progress },
                    color = MaterialTheme.colors.surface,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = DecimalFormat.format(progress * 100) + "%",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

private val Habit.Priority.color: Color
    get() = when (this) {
        Habit.Priority.Low -> Green400
        Habit.Priority.Medium -> Orange400
        Habit.Priority.High -> Red400
    }

@Preview
@Composable
fun HabitItemPreview() {
    MyHabitTheme {
        HabitCard(
            title = "Jogging",
            progress = 0.5F,
            priority = Habit.Priority.Low,
            focusTimes = 20.minutes,
            onClickCard = {},
            onClickDetail = {},
            onClickDelete = {}
        )
    }
}
