package bagus2x.myhabit.presentation.countdown

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.presentation.common.component.ProgressIndicator
import bagus2x.myhabit.presentation.common.theme.Green400
import bagus2x.myhabit.presentation.common.theme.Orange400
import bagus2x.myhabit.presentation.common.theme.Red400
import bagus2x.myhabit.presentation.main.LocalMainScaffoldState
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration

private val TweenLinearEasing = tween<Float>(
    durationMillis = 1000,
    easing = LinearEasing
)

@Composable
fun CountDownScreen(viewModel: CountDownViewModel = hiltViewModel()) {
    val scaffoldState = LocalMainScaffoldState.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is CountdownEvent.Snackbar -> scaffoldState.snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    CountDownScreen(
        uiStateProvider = { uiState },
        onClickButtonStop = viewModel::stop,
        onClickButtonStart = viewModel::start
    )
}

@Composable
fun CountDownScreen(
    uiStateProvider: () -> CountdownState,
    onClickButtonStop: () -> Unit,
    onClickButtonStart: () -> Unit
) {
    val currentView = LocalView.current

    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        val state = uiStateProvider()
        val context = LocalContext.current
        val timer = remember { Animatable(0F) }
        if (state.habit != null) {
            BackHandler(
                enabled = state.isRunning,
                onBack = {
                    Toast.makeText(context, "Cannot go back", Toast.LENGTH_SHORT).show()
                }
            )
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.habit.title.uppercase(),
                    style = MaterialTheme.typography.h5
                )
                Text(text = state.habit.description)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                LaunchedEffect(state.timesRemaining) {
                    timer.animateTo(
                        targetValue = state.liveProgress,
                        animationSpec = TweenLinearEasing
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = state.timesRemaining.text,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(state.habit.priority.color)
                    )
                }
                ProgressIndicator(
                    progressProvider = timer::value,
                    modifier = Modifier.size(240.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            if (state.isRunning) {
                Surface(
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    shape = CircleShape
                ) {
                    IconButton(onClick = onClickButtonStop) {
                        Icon(
                            imageVector = Icons.Outlined.Stop,
                            contentDescription = "Start countdown"
                        )
                    }
                }
            } else {
                Surface(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    shape = CircleShape
                ) {
                    IconButton(onClick = onClickButtonStart) {
                        Icon(
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = "Start countdown"
                        )
                    }
                }
            }
        }
    }
}

private val CountdownState.liveProgress: Float
    get() {
        if (habit != null) {
            return ((habit.focusTimes - timesRemaining) / habit.focusTimes).toFloat()
        }
        return 0F
    }

private val Habit.Priority.color: Color
    get() = when (this) {
        Habit.Priority.Low -> Green400
        Habit.Priority.Medium -> Orange400
        Habit.Priority.High -> Red400
    }

private val Duration.text: String
    get() = toComponents { hours, minutes, seconds, _ ->
        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }
