package org.example.project.screens.asker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.data.AnswerDTO
import org.example.project.data.QuestionDTO
import org.example.project.screens.result.Result
import org.example.project.screens.result.ResultScreen
import org.example.project.theme.MyApplicationTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

class AskerScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<AskerViewModel>()
        val navigator = LocalNavigator.current
        AskerScreen(screenModel) { totalAns, rightAns -> navigator?.push(Result(totalAns, rightAns)) }
    }
}


@Composable
fun AskerScreen(viewModel: AskerViewModel, onFinish: (Int, Int) -> Unit) {
    val navigator = LocalNavigator.current
    MyApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val questions = viewModel.uiState.collectAsState().value

            if (questions != null)
                QuestionsScreen(
                    questions = questions,
                    onDonePressed = {
                        val totalQuestions = questions.questionsState.count()
                        val rightAnswers =
                            questions.questionsState.count { it.givenAnswerId == it.question.correctAnswerId }
                        onFinish.invoke(totalQuestions, rightAnswers)
                    },
                    onBackPressed = { navigator?.pop() }
                )
            else {

            }
        }
    }
}

@Composable
fun QuestionsScreen(
    questions: Questions,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit
) {
    val questionState = remember(questions.currentQuestionIndex) {
        questions.questionsState[questions.currentQuestionIndex]
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                QuizTopAppBar(
                    questionIndex = questionState.questionIndex,
                    totalQuestionsCount = questionState.totalQuestions,
                    onBackPressed = onBackPressed
                )
            },
            content = { innerPadding ->

                QuestionContent(
                    question = questionState.question,
                    selectedAnswer = questionState.givenAnswerId,
                    onAnswer = {
                        questionState.givenAnswerId = it.id
                        questionState.enableNext = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    drawableResId = questionState.drawableResId
                )
            },
            bottomBar = {
                NavigationButtons(
                    questionState = questionState,
                    onPreviousPressed = { questions.currentQuestionIndex-- },
                    onNextPressed = { questions.currentQuestionIndex++ },
                    onDonePressed = onDonePressed
                )
            }
        )
    }
}

@Composable
private fun QuestionContent(
    question: QuestionDTO,
    selectedAnswer: String,
    onAnswer: (AnswerDTO) -> Unit,
    modifier: Modifier = Modifier,
    drawableResId: DrawableResource? = null
) {
    var isImageVisible by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            QuestionTitle(question.question)
            Spacer(modifier = Modifier.height(24.dp))

            drawableResId?.let {
                IconButton(
                    onClick = { isImageVisible = !isImageVisible }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = if (isImageVisible) "Hide image" else "Show image"
                    )
                }

                AnimatedVisibility(visible = isImageVisible) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(resource = drawableResId),
                            contentDescription = "Question image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            SingleChoiceQuestion(
                question = question,
                selectedAnswer = selectedAnswer,
                onAnswerSelected = { answer -> onAnswer(answer) },
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}


@Composable
private fun QuestionTitle(title: String) {
    val backgroundColor = MaterialTheme.colorScheme.background
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
    }
}

@Composable
private fun SingleChoiceQuestion(
    question: QuestionDTO,
    selectedAnswer: String = "",
    onAnswerSelected: (AnswerDTO) -> Unit,
    modifier: Modifier = Modifier
) {
    val radioOptions = question.answers

    val (selectedOption, onOptionSelected) = remember(selectedAnswer) {
        mutableStateOf(selectedAnswer)
    }

    Column(modifier = modifier) {
        radioOptions.forEach { answer ->
            val onClickHandle = {
                onOptionSelected(answer.id)
                onAnswerSelected(answer)
            }

            val optionSelected = answer.id == selectedOption

            val answerBorderColor = if (optionSelected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            }
            val answerBackgroundColor = if (optionSelected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colorScheme.background
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = optionSelected,
                            onClick = onClickHandle
                        )
                        .background(answerBackgroundColor)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(4f),
                        text = answer.text
                    )

                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = optionSelected,
                        onClick = onClickHandle,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizTopAppBar(
    questionIndex: Int,
    totalQuestionsCount: Int,
    onBackPressed: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (questionIndex + 1) / totalQuestionsCount.toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )
    Column {
        TopAppBar(
            title = {
                TopAppBarTitle(
                    questionIndex = questionIndex,
                    totalQuestionsCount = totalQuestionsCount,
                )
            },
            actions = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.padding(vertical = 20.dp)
        )

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

    }


}

@Composable
private fun TopAppBarTitle(
    questionIndex: Int,
    totalQuestionsCount: Int,
    modifier: Modifier = Modifier
) {
    val indexStyle = MaterialTheme.typography.titleLarge.toSpanStyle()
    val totalStyle = MaterialTheme.typography.titleSmall.toSpanStyle()
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("${questionIndex + 1} ")
        }
        withStyle(style = totalStyle) {
            append("z $totalQuestionsCount")
        }
    }
    Text(
        text = text,
        modifier = modifier
    )
}


@Composable
private fun NavigationButtons(
    questionState: QuestionState,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (questionState.showPrevious) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onPreviousPressed
                ) {
                    Text(text = "Wstecz")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.primary),
                enabled = questionState.enableNext,
                onClick = if (questionState.showDone)
                    onDonePressed
                else
                    onNextPressed
            ) {
                if (questionState.showDone)
                    Text(text = "Zakończ")
                else
                    Text("Dalej")
            }
        }
    }
}
