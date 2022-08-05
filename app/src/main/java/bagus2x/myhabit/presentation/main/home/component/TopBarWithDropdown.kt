package bagus2x.myhabit.presentation.main.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bagus2x.myhabit.R

@Composable
fun TopBarWithDropdown(
    selectedMenu: String,
    menus: List<String>,
    onClickMenu: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .statusBarsPadding()
    ) {
        Text(
            text = stringResource(R.string.text_this_week),
            modifier = Modifier
                .weight(1F),
            style = MaterialTheme.typography.h5,
        )
        var expanded by rememberSaveable { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expanded = !expanded }
        ) {
            val scope = rememberCoroutineScope()
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                menus.forEachIndexed { index, menu ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onClickMenu(index)
                        }
                    ) {
                        Text(text = menu)
                    }
                }
            }
            Text(text = selectedMenu)
            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = null
            )
        }
    }
}
