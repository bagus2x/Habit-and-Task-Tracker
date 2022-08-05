package bagus2x.myhabit.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import bagus2x.myhabit.presentation.countdown.CountDownScreen
import bagus2x.myhabit.presentation.habit.HabitScreen
import bagus2x.myhabit.presentation.main.component.MainTab
import bagus2x.myhabit.presentation.main.home.HomeScreen
import bagus2x.myhabit.presentation.main.profile.ProfileScreen
import bagus2x.myhabit.presentation.task.TaskScreen

sealed class Destination(val path: String) {

    object Main : Destination("main") {
        val home = "$path/home"
        val profile = "$path/profile"

        operator fun invoke() = path
    }

    object Habit : Destination("habit?$KEY_HABIT_ID={$KEY_HABIT_ID}") {
        val arguments = buildList {
            navArgument(KEY_HABIT_ID) { type = NavType.IntType }.apply(::add)
        }

        operator fun invoke(habitId: Int = 0) = "habit?$KEY_HABIT_ID=$habitId"
    }

    object Countdown : Destination("countdown/{$KEY_HABIT_ID}") {
        val arguments = buildList {
            navArgument(KEY_HABIT_ID) { type = NavType.IntType }.apply(::add)
        }

        operator fun invoke(habitId: Int) = "countdown/$habitId"
    }

    object Task : Destination("task?$KEY_TASK_ID={$KEY_TASK_ID}") {
        val arguments = buildList {
            navArgument(KEY_TASK_ID) { type = NavType.LongType }.apply(::add)
        }

        operator fun invoke(habitId: Long = 0) = "task?$KEY_TASK_ID=$habitId"
    }

    companion object {
        const val KEY_HABIT_ID = "habitId"
        const val KEY_TASK_ID = "taskId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Destination.Main()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(
            route = Destination.Main.path,
            startDestination = MainTab.Home.route
        ) {
            composable(MainTab.Home.route) {
                HomeScreen(navController)
            }
            composable(MainTab.Profile.route) {
                ProfileScreen(navController)
            }
        }
        composable(Destination.Habit.path, Destination.Habit.arguments) {
            HabitScreen(navController)
        }
        composable(Destination.Countdown.path, Destination.Countdown.arguments) {
            CountDownScreen()
        }
        composable(Destination.Task.path, Destination.Task.arguments) {
            TaskScreen(navController)
        }
    }
}
