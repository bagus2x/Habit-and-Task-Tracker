package bagus2x.myhabit.presentation.task.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun TopBarMenu(
    modifier: Modifier = Modifier,
    onClickMenuAddChild: () -> Unit,
    onClickMenuDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onClickMenuAddChild()
                }
            ) {
                Text(text = "Add Child")
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onClickMenuDelete()
                }
            ) {
                Text(text = "Delete All")
            }
        }
    }
}
