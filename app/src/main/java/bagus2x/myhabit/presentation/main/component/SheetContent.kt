package bagus2x.myhabit.presentation.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bagus2x.myhabit.R
import bagus2x.myhabit.presentation.main.Destination

@Composable
fun SheetContent(modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    val item = @Composable { title: String, icon: ImageVector, onClickItem: () -> Unit ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClickItem)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(start = 12.dp)
            )
            Text(
                text = title, color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.button,
                modifier = Modifier
                    .weight(1F)
                    .padding(end = 12.dp)
            )
        }
    }

    Column(modifier = modifier) {
        item(
            stringResource(R.string.add_habit),
            Icons.Outlined.Timer
        ) { onClick(Destination.Habit()) }
        item(
            stringResource(R.string.add_task),
            Icons.Outlined.AddTask
        ) { onClick(Destination.Task()) }
    }
}
