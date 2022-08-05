package bagus2x.myhabit.presentation.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bagus2x.myhabit.presentation.common.theme.MyHabitTheme
import bagus2x.myhabit.presentation.main.component.MainTab
import bagus2x.myhabit.presentation.main.component.MyHabitBottomBar
import bagus2x.myhabit.presentation.main.component.SheetContent
import kotlinx.coroutines.launch

val LocalMainScaffoldState = staticCompositionLocalOf {
    ScaffoldState(
        drawerState = DrawerState(DrawerValue.Closed),
        snackbarHostState = SnackbarHostState()
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    MyHabitTheme {
        val navController = rememberNavController()
        val tabs = remember { MainTab.values() }
        val scaffoldState = rememberScaffoldState()
        val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val isBottomBarVisible by remember {
            derivedStateOf {
                currentBackStack
                    ?.destination
                    ?.hierarchy
                    ?.any { it.route == Destination.Main() } ?: false
            }
        }
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                SheetContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 32.dp),
                    onClick = { destination ->
                        navController.navigate(destination)
                        scope.launch { sheetState.hide() }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    AnimatedVisibility(
                        visible = isBottomBarVisible,
                        enter = slideInVertically(initialOffsetY = { it }) + expandVertically { it },
                        exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically { -it }
                    ) {
                        MyHabitBottomBar(
                            navController = navController,
                            tabs = tabs
                        )
                    }
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = isBottomBarVisible,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { 2 * it })
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.surface)
                        ) {
                            FloatingActionButton(
                                onClick = { scope.launch { sheetState.show() } },
                                contentColor = MaterialTheme.colors.surface,
                                shape = CircleShape,
                                elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 0.dp,
                                    pressedElevation = 0.dp,
                                    hoveredElevation = 0.dp,
                                    focusedElevation = 0.dp
                                ),
                                modifier = Modifier.padding(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.background(MaterialTheme.colors.background)
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    CompositionLocalProvider(LocalMainScaffoldState provides scaffoldState) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun MainScreenPreview() {
    MyHabitTheme { MainScreen() }
}
