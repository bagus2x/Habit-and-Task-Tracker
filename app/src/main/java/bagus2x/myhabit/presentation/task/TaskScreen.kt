package bagus2x.myhabit.presentation.task

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import bagus2x.myhabit.R
import bagus2x.myhabit.domain.model.Task
import bagus2x.myhabit.presentation.common.component.BasicTextField
import bagus2x.myhabit.presentation.common.component.TitleTextField
import bagus2x.myhabit.presentation.main.Destination
import bagus2x.myhabit.presentation.main.LocalMainScaffoldState
import bagus2x.myhabit.presentation.task.component.ChildTask
import bagus2x.myhabit.presentation.task.component.TaskTopBar
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val HeaderCardShape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun TaskScreen(
    navController: NavHostController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val scaffoldState = LocalMainScaffoldState.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is TaskEvent.Snackbar -> scaffoldState.snackbarHostState.showSnackbar(event.message)
                is TaskEvent.PopBackStack -> navController.popBackStack()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TaskScreen(
        uiStateProvider = { uiState },
        onChangeTitle = viewModel::changeTitle,
        onChangeDescription = viewModel::changeDescription,
        childrenTaskState = viewModel.childrenTask,
        onClickButtonBack = navController::popBackStack,
        onChangeDate = viewModel::changeDate,
        onChangeTime = viewModel::changeTime,
        onClickButtonSave = viewModel::saveUpdatedTask,
        onClickTask = { navController.navigate(Destination.Task(it)) },
        onClickAddTask = viewModel::createChildTask,
        onClickDeleteTask = viewModel::deleteTask
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(
    uiStateProvider: () -> TaskState,
    onChangeTitle: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    childrenTaskState: StateFlow<PagingData<Pair<Task, Int>>>,
    onClickButtonBack: () -> Unit,
    onChangeDate: (LocalDate) -> Unit,
    onChangeTime: (LocalTime) -> Unit,
    onClickButtonSave: () -> Unit,
    onClickTask: (Long) -> Unit,
    onClickAddTask: (Long) -> Unit,
    onClickDeleteTask: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            val state = uiStateProvider()
            TaskTopBar(
                savable = state.isChanged,
                onClickButtonBack = onClickButtonBack,
                dateTime = state.parentTask?.due,
                onChangeDate = onChangeDate,
                onChangeTime = onChangeTime,
                onClickButtonSave = onClickButtonSave,
                onClickMenuAddChild = { state.parentTask?.taskId?.let(onClickAddTask) },
                onClickMenuDelete = { state.parentTask?.taskId?.let(onClickDeleteTask) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
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
                    text = if (state.isModeEdit) stringResource(R.string.text_edit_task)
                    else stringResource(R.string.text_create_task),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                TitleTextField(
                    value = state.parentTask?.title ?: "",
                    onChange = { onChangeTitle(it) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    placeholder = { Text(text = stringResource(R.string.text_task_title)) }
                )
                BasicTextField(
                    value = state.parentTask?.description ?: "",
                    onValueChange = { onChangeDescription(it) },
                    placeholder = { Text(text = stringResource(R.string.text_add_task_details)) },
                    textStyle = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 32.dp)
                        .fillMaxWidth()
                )
            }
            val childrenTask = childrenTaskState.collectAsLazyPagingItems()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                if (childrenTask.itemCount == 0) {
                    item {
                        Text(
                            text = "There is no child task available",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)
                        )
                    }
                }
                items(items = childrenTask, key = { it.first.taskId }) { taskAndIndent ->
                    if (taskAndIndent != null) {
                        val (task, indent) = taskAndIndent
                        ChildTask(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            title = task.title,
                            indent = indent,
                            onClick = { onClickTask(task.taskId) },
                            onClickAddChild = { onClickAddTask(task.taskId) },
                            onClickDelete = { onClickDeleteTask(task.taskId) }
                        )
                    }
                }
            }
        }
    }
}
