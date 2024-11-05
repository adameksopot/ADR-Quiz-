import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.screens.asker.AskerScreen
import org.example.project.screens.welcome.WelcomeViewModel
import org.jetbrains.compose.resources.painterResource
import quizkmp.composeapp.generated.resources.Res
import quizkmp.composeapp.generated.resources.adr_icon
import quizkmp.composeapp.generated.resources.compose_multiplatform

class WelcomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel = koinScreenModel<WelcomeViewModel>()
        WelcomeScreen(
            welcomeViewModel = screenModel,
            onNavigateForward = { navigator?.push(AskerScreen()) },
            onRetry = {})
    }
}


@Composable
fun WelcomeScreen(
    welcomeViewModel: WelcomeViewModel,
    onNavigateForward: () -> Unit,
    onRetry: () -> Unit,
) {
    welcomeViewModel.loadQuestions()
    val viewState = welcomeViewModel.state
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WelcomeLogo()
        WelcomeButton(
            viewState = viewState.value,
            onRetry = { onRetry.invoke() },
            onStart = { onNavigateForward.invoke() }
        )
    }
}


@Composable
fun WelcomeButton(
    viewState: WelcomeViewModel.State,
    onRetry: () -> Unit,
    onStart: () -> Unit
) {
    when (viewState) {
        WelcomeViewModel.State.Loading -> CircularProgressIndicator()
        WelcomeViewModel.State.Error ->
            OutlinedButton(onClick = onRetry) {
                Text(text = "Spróbuj ponownie")
            }

        WelcomeViewModel.State.Success ->
            OutlinedButton(onClick = onStart) {
                Text(text = "Rozpocznij Test!")
            }
    }
}


@Composable
fun WelcomeLogo() {
    Image(
        painterResource(resource = Res.drawable.adr_icon),
        contentDescription = "Adr icon"
    )
}


