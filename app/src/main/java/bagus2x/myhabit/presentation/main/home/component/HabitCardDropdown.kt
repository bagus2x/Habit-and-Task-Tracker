package bagus2x.myhabit.presentation.main.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun HabitCardDropdown(
    expanded: Boolean,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onClickDetail: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Icon(
            imageVector = Icons.Outlined.MoreHoriz,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onClick)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            DropdownMenuItem(onClick = onClickDetail) {
                Text(text = "Edit")
            }
            DropdownMenuItem(onClick = onClickDelete) {
                Text(text = "Delete")
            }
        }
    }
}
