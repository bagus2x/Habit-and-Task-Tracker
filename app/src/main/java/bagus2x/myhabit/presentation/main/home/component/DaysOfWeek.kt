package bagus2x.myhabit.presentation.main.home.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import bagus2x.myhabit.presentation.common.component.coloredShadow
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

private var DaysOfWeekPadding = PaddingValues(horizontal = 16.dp)
private const val MAX_ROW_ITEM = 7

@Composable
fun DaysOfWeek(
    selected: LocalDateTime?,
    onChange: (LocalDateTime?) -> Unit,
    firstDayOfWeek: LocalDateTime,
    modifier: Modifier = Modifier,
    selectedBorderStroke: BorderStroke = BorderStroke(1.dp, MaterialTheme.colors.primary)
) {
    val now = remember { LocalDateTime.now() }
    val days = mutableListOf<LocalDateTime>().apply {
        repeat(MAX_ROW_ITEM) { firstDayOfWeek.plusDays(it.toLong()).apply(::add) }
    }
    val lazyListState = rememberLazyListState()
    LazyRow(
        modifier = modifier,
        state = lazyListState,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = DaysOfWeekPadding
    ) {
        items(items = days, key = { it.hashCode() }) { date ->
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val dayOfMonth = date.dayOfMonth.toString()
            val isSelected = selected == date
            val isToday = date.dayOfYear == now.dayOfYear && date.year == now.year
            val color = if (isToday) MaterialTheme.colors.primary else MaterialTheme.colors.surface
            var isPressed by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(targetValue = if (isPressed) 1.2F else 1.0F)
            Surface(
                modifier = Modifier
                    .widthIn(min = 54.dp)
                    .scale(scale)
                    .run {
                        if (isToday) this.coloredShadow(
                            color = MaterialTheme.colors.primary,
                            alpha = 0.25F
                        )
                        else this
                    },
                color = color,
                shape = MaterialTheme.shapes.medium,
                border = if (isSelected) selectedBorderStroke else null
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .pointerInput(isSelected) {
                            detectTapGestures(
                                onTap = { onChange(if (isSelected && !isToday) null else date) },
                                onPress = {
                                    isPressed = true
                                    awaitRelease()
                                    isPressed = false
                                }
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayOfWeek,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = dayOfMonth,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
}
