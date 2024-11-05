package org.example.project.screens.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.theme.MyApplicationTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import quizkmp.composeapp.generated.resources.Res
import quizkmp.composeapp.generated.resources.result_failed
import quizkmp.composeapp.generated.resources.result_passed
import quizkmp.composeapp.generated.resources.result_score_result
import quizkmp.composeapp.generated.resources.welcome_button_retry

class Result(private val totalQuestions: Int, private val rightAnswers: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        ResultScreen(totalQuestions, rightAnswers, onRetry = { navigator?.popUntilRoot() })
    }
}

@Composable
fun ResultScreen(
    totalQuestions: Int,
    rightAnswers: Int,
    onRetry: () -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val percentageScore = (rightAnswers * 100) / totalQuestions

            Text(
                textAlign = TextAlign.Center,
                text = stringResource(
                    Res.string.result_score_result,
                    percentageScore,
                    totalQuestions
                ),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )

            if (percentageScore >= 60) {
                Text(
                    text = stringResource(Res.string.result_passed),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Text(
                    text = stringResource(Res.string.result_failed),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

            }
            Button(onClick = { onRetry() }) {
                Text(text = stringResource(Res.string.welcome_button_retry))
            }
        }
    }
}

@Preview
@Composable
fun preview() {
    MyApplicationTheme {
        ResultScreen(totalQuestions = 5, rightAnswers = 3, onRetry = {})
    }
}
