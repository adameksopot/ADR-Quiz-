package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class QuestionsDTO(
    val questions: List<QuestionDTO>
)
@Serializable
data class QuestionDTO(
    val question: String,
    val answers: List<AnswerDTO>,
    val correctAnswerId: String,
    val answerExplanation: String,

    )
@Serializable
data class AnswerDTO(
    val id: String,
    val text: String
)