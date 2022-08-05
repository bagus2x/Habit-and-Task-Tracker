package bagus2x.myhabit.presentation.task.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
private val FractionalThreshold = FractionalThreshold(0.4F)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChildTask(
    modifier: Modifier = Modifier,
    title: String,
    indent: Int,
    onClick: () -> Unit,
    onClickDelete: () -> Unit,
    onClickAddChild: () -> Unit
) {
    val dismissState = rememberDismissState()
    val scope = rememberCoroutineScope()
    val cancel: () -> Unit = { scope.launch { dismissState.reset() } }
    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        dismissThresholds = { FractionalThreshold },
        background = {
            BackgroundAction(
                onClickCancel = cancel,
                onClickDelete = onClickDelete,
                onClickAddChild = {
                    onClickAddChild()
                    cancel()
                }
            )
        },
        dismissContent = {
            DismissContent(
                modifier = Modifier.clickable(onClick = onClick),
                title = title,
                indent = indent
            )
        }
    )
}

@Composable
private fun DismissContent(
    modifier: Modifier = Modifier,
    title: String,
    indent: Int
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
            .childrenLine(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2F),
                indent = indent
            )
    ) {
        Row(
            modifier = Modifier.padding(
                start = (indent * 16).dp + 16.dp,
                top = 16.dp,
                bottom = 16.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (indent == 0) {
                Box(
                    modifier = Modifier
                        .offset(x = (-4).dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text = title.ifBlank { "Untitled" })
        }
    }
}

@Composable
private fun BackgroundAction(
    modifier: Modifier = Modifier,
    onClickCancel: () -> Unit,
    onClickDelete: () -> Unit,
    onClickAddChild: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colors.onBackground.copy(0.1F))
//            .borderBottom(color = MaterialTheme.colors.background)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onClickCancel) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null
            )
        }
        IconButton(onClick = onClickDelete) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null
            )
        }
        IconButton(onClick = onClickAddChild) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null
            )
        }
    }
}

private fun Modifier.childrenLine(
    width: Dp = 1.dp,
    color: Color,
    indent: Int
): Modifier {
    return drawBehind {
        val strokeWidth = width.value * density
        repeat(indent) {
            val x = 16.dp.value * density + 16.dp.value * density * it
            drawLine(
                color = color,
                start = Offset(x = x, y = 0F),
                end = Offset(x = x, y = size.height),
                strokeWidth = strokeWidth
            )
        }
    }
}
