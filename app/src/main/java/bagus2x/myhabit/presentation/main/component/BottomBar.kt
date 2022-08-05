package bagus2x.myhabit.presentation.main.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import bagus2x.myhabit.R
import bagus2x.myhabit.presentation.main.Destination

private var BottomBarShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
private val BottomBarHeight = 56.dp

enum class MainTab(
    @StringRes
    val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
) {
    Home(
        titleRes = R.string.text_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = Destination.Main.home
    ),
    Profile(
        titleRes = R.string.text_profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        route = Destination.Main.profile
    )
}

@Composable
fun MyHabitBottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    tabs: Array<MainTab>
) {
    BottomNavigation(
        modifier = modifier
            .clip(BottomBarShape)
            .windowInsetsBottomHeight(
                WindowInsets.navigationBars.add(WindowInsets(bottom = BottomBarHeight))
            ),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: MainTab.Home.route

        tabs.forEach { tab ->
            val isSelected = currentRoute == tab.route

            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    if (tab.route == currentRoute) {
                        return@BottomNavigationItem
                    }
                    navController.navigate(tab.route) {
                        popUpTo(MainTab.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = stringResource(tab.titleRes)
                    )
                },
                modifier = Modifier.navigationBarsPadding(),
                label = { Text(text = stringResource(tab.titleRes)) }
            )
        }
    }
}
