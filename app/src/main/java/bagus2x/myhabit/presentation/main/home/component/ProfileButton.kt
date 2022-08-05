package bagus2x.myhabit.presentation.main.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier,
    username: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy((-12).dp),
    ) {
        Surface(
            modifier = Modifier.zIndex(1F),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colors.onSurface,
            border = BorderStroke(1.dp, MaterialTheme.colors.surface)
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.surface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
    }
}

@Preview
@Composable
fun ProfileButtonPreview() {
    MyHabitTheme {
        ProfileButton(username = "Tubagus")
    }
}
