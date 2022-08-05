package bagus2x.myhabit.presentation.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import bagus2x.myhabit.R
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.model.Task
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme
import bagus2x.myhabit.presentation.main.Destination
import bagus2x.myhabit.presentation.main.home.component.DaysOfWeek
import bagus2x.myhabit.presentation.main.home.component.HabitCard
import bagus2x.myhabit.presentation.main.home.component.TaskCard
import bagus2x.myhabit.presentation.main.home.component.TopBarWithDropdown
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.minutes

private val HeaderCardShape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    HomeScreen(
        uiStateProvider = state::value,
        habitsState = viewModel.habits,
        tasksState = viewModel.tasks,
        onChangeLocalDate = viewModel::selectLocalDateTime,
        onClickHabitItem = { navController.navigate(Destination.Countdown(it)) },
        onClickDetailHabit = { navController.navigate(Destination.Habit(it)) },
        onClickDeleteHabit = viewModel::deleteHabit,
        onClickTaskItem = { navController.navigate(Destination.Task(it)) }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    uiStateProvider: () -> HomeState,
    habitsState: StateFlow<PagingData<Habit.HabitWithProgress>>,
    tasksState: StateFlow<PagingData<Task>>,
    onChangeLocalDate: (LocalDateTime?) -> Unit,
    onClickHabitItem: (Int) -> Unit,
    onClickDetailHabit: (Int) -> Unit,
    onClickDeleteHabit: (Int) -> Unit,
    onClickTaskItem: (Long) -> Unit,
) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            val pagerState = rememberPagerState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(HeaderCardShape)
                    .background(MaterialTheme.colors.surface),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val state = uiStateProvider()
                val scope = rememberCoroutineScope()
                val context = LocalContext.current
                val dropDownMenus = remember {
                    buildList {
                        add(context.getString(R.string.text_habit))
                        add(context.getString(R.string.text_task))
                    }
                }
                TopBarWithDropdown(
                    selectedMenu = dropDownMenus[pagerState.currentPage],
                    menus = dropDownMenus,
                    onClickMenu = { index ->
                        scope.launch { pagerState.animateScrollToPage(index) }
                    }
                )
                DaysOfWeek(
                    selected = state.selectedDate,
                    onChange = onChangeLocalDate,
                    firstDayOfWeek = state.firstDayOfWeek,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                )
            }
            HorizontalPager(
                state = pagerState,
                count = 2,
                modifier = Modifier.weight(1F)
            ) { index ->
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sleepy_cat))
                if (index == 0) {
                    val habitsWithProgress = habitsState.collectAsLazyPagingItems()
                    val isEmpty by remember { derivedStateOf { habitsWithProgress.itemCount == 0 } }
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(count = habitsWithProgress.itemCount) { index ->
                                val habitWithProgress = habitsWithProgress[index] ?: return@items
                                val habit = habitWithProgress.habit
                                val progress = habitWithProgress.progress
                                HabitCard(
                                    title = habit.title,
                                    priority = habit.priority,
                                    focusTimes = habit.focusTimes,
                                    progress = progress,
                                    onClickCard = { onClickHabitItem(habit.habitId) },
                                    onClickDetail = { onClickDetailHabit(habit.habitId) },
                                    onClickDelete = { onClickDeleteHabit(habit.habitId) },
                                    modifier = Modifier.animateItemPlacement()
                                )
                            }
                        }
                        if (isEmpty) {
                            LottieAnimation(
                                composition = composition,
                                modifier = Modifier
                                    .width(240.dp)
                                    .align(Alignment.Center),
                                iterations = LottieConstants.IterateForever
                            )
                        }
                    }
                }
                if (index == 1) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val tasks = tasksState.collectAsLazyPagingItems()
                        val isEmpty by remember { derivedStateOf { tasks.itemCount == 0 } }
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(tasks) { task ->
                                if (task != null) {
                                    TaskCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        title = task.title,
                                        onClick = { onClickTaskItem(task.taskId) }
                                    )
                                }
                            }
                        }
                        if (isEmpty) {
                            LottieAnimation(
                                composition = composition,
                                modifier = Modifier
                                    .width(240.dp)
                                    .align(Alignment.Center),
                                iterations = LottieConstants.IterateForever
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun HomeScreenPreview() {
    val currentDate = LocalDateTime.now()
    val dummyHabits = List(10) {
        Habit.HabitWithProgress(
            habit = Habit(
                habitId = it,
                title = "Habit title $it",
                description = "Description",
                priority = Habit.Priority.values().random(),
                focusTimes = 5.minutes,
                dayOfWeek = currentDate.dayOfWeek,
                createdAt = currentDate
            ),
            progress = 0.7F,
        )
    }
    MyHabitTheme {
        HomeScreen(
            uiStateProvider = { HomeState.Default },
            habitsState = MutableStateFlow(PagingData.from(dummyHabits)),
            tasksState = MutableStateFlow(PagingData.empty()),
            onChangeLocalDate = {},
            onClickHabitItem = {},
            onClickDetailHabit = {},
            onClickDeleteHabit = {},
            onClickTaskItem = {}
        )
    }
}
