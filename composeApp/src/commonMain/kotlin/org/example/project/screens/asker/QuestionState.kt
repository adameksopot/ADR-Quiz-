package org.example.project.screens.asker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.example.project.data.QuestionDTO
import org.jetbrains.compose.resources.DrawableResource

data class Questions(
    val questionsState: List<QuestionState>
) {
    var currentQuestionIndex by mutableIntStateOf(0)

    companion object {
        val EMPTY = Questions(emptyList())
    }
}



class QuestionState(
    val question: QuestionDTO,
    val questionIndex: Int,
    val totalQuestions: Int,
    val showPrevious: Boolean,
    val showDone: Boolean,
    val drawableResId: DrawableResource?

) {
    var enableNext by mutableStateOf(false)
    var givenAnswerId by mutableStateOf("")
}