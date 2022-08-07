package bagus2x.myhabit.presentation.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import bagus2x.myhabit.R
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.presentation.common.component.BasicTextField
import bagus2x.myhabit.presentation.common.component.TitleTextField
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme
import bagus2x.myhabit.presentation.habit.component.DayOfWeekPicker
import bagus2x.myhabit.presentation.habit.component.FocusTimePicker
import bagus2x.myhabit.presentation.habit.component.PriorityPicker
import bagus2x.myhabit.presentation.main.LocalMainScaffoldState
import kotlinx.coroutines.flow.collectLatest
import java.time.DayOfWeek
import kotlin.time.Duration

private val HeaderCardShape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)

@Composable
fun HabitScreen(
    navController: NavHostController,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val scaffoldState = LocalMainScaffoldState.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is HabitEvent.Snackbar -> scaffoldState.snackbarHostState.showSnackbar(event.message)
                HabitEvent.PopBackStack -> navController.popBackStack()
            }
        }
    }
    val uiState = viewModel.uiState.collectAsState()
    HabitScreen(
        uiStateProvider = uiState::value,
        onClickButtonBack = viewModel::popBackStack,
        onClickButtonCreate = viewModel::createOrUpdate,
        onChangeTitle = viewModel::changeTitle,
        onChangeDescription = viewModel::changeDescription,
        onChangePriority = viewModel::changePriority,
        onChangeFocusTime = viewModel::changeFocusTime,
        onChangeDayOfWeek = viewModel::changeDayOfWeek
    )
}

@Composable
fun HabitScreen(
    uiStateProvider: () -> HabitState,
    onClickButtonBack: () -> Unit,
    onClickButtonCreate: () -> Unit,
    onChangeTitle: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    onChangePriority: (Habit.Priority) -> Unit,
    onChangeFocusTime: (Duration) -> Unit,
    onChangeDayOfWeek: (DayOfWeek) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp,
                contentPadding = WindowInsets.statusBars.asPaddingValues(LocalDensity.current)
            ) {
                IconButton(onClick = onClickButtonBack) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
                IconButton(onClick = onClickButtonCreate) {
                    Icon(
                        imageVector = Icons.Outlined.Done,
                        contentDescription = null,
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val state = uiStateProvider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(HeaderCardShape)
                    .background(MaterialTheme.colors.surface),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (state.isModeEdit) "Edit Habit" else stringResource(R.string.text_create_new_habit),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                TitleTextField(
                    value = state.title,
                    onChange = onChangeTitle,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    placeholder = { Text(text = stringResource(R.string.text_habit_title)) },
                    textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
                )
                BasicTextField(
                    value = state.description,
                    onValueChange = onChangeDescription,
                    placeholder = { Text(text = stringResource(R.string.text_add_habit_details)) },
                    textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 32.dp)
                        .fillMaxWidth()
                )
            }
            Text(
                text = stringResource(R.string.text_choose_priority),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 16.dp, top = 32.dp)
            )
            PriorityPicker(
                value = state.priority,
                onChange = onChangePriority,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            Text(
                text = stringResource(R.string.text_focus_time),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 16.dp, top = 32.dp)
            )
            FocusTimePicker(
                value = state.focusTime,
                onChange = onChangeFocusTime,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            Text(
                text = stringResource(R.string.text_on_these_days),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 16.dp, top = 32.dp)
            )
            DayOfWeekPicker(
                value = state.dayOfWeek,
                onChange = onChangeDayOfWeek,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun HabitScreenPreview() {
    MyHabitTheme {
        HabitScreen(
            uiStateProvider = { HabitState.Default },
            onClickButtonBack = {},
            onClickButtonCreate = {},
            onChangeTitle = {},
            onChangeDescription = {},
            onChangePriority = {},
            onChangeFocusTime = {},
            onChangeDayOfWeek = {},
        )
    }
}
