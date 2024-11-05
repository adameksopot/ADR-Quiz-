package org.example.project.screens.asker

import org.example.project.data.QuestionDTO

class QuestionMapper  constructor() {
    fun map(question: QuestionDTO, index: Int, totalQuestions: Int): QuestionState {
        val drawableResId = null


        val showPrevious = index > 0
        val showDone = index == totalQuestions - 1

        return QuestionState(
            question = question,
            questionIndex = index,
            totalQuestions = totalQuestions,
            showPrevious = showPrevious,
            showDone = showDone,
            drawableResId = drawableResId
        )
    }
}
