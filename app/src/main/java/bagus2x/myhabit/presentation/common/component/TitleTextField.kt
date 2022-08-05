package bagus2x.myhabit.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import bagus2x.myhabit.R
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme

@Composable
fun TitleTextField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.body1,
) {
    ConstraintLayout(modifier = modifier) {
        val (indicatorRef, textFieldRef) = createRefs()
        Box(
            modifier = Modifier
                .width(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.primary)
                .constrainAs(indicatorRef) {
                    start.linkTo(parent.start)
                    top.linkTo(textFieldRef.top)
                    bottom.linkTo(textFieldRef.bottom)
                    height = Dimension.fillToConstraints
                }
        )
        BasicTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.constrainAs(textFieldRef) {
                start.linkTo(indicatorRef.end, 12.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            placeholder = placeholder,
            textStyle = textStyle
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TitleTextFieldPreview() {
    MyHabitTheme {
        TitleTextField(
            value = "",
            onChange = { },
            placeholder = { Text(text = stringResource(R.string.text_habit_title)) }
        )
    }
}
