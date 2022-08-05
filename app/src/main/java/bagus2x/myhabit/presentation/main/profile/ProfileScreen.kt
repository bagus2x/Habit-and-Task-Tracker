package bagus2x.myhabit.presentation.main.profile

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Button(
        onClick = {

        },
        modifier = Modifier.statusBarsPadding()
    ) {
        Text(
            text = "Coming soon",
        )
    }
}
