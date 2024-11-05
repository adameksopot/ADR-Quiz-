package org.example.project.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import quizkmp.composeapp.generated.resources.Res


class QuestionRepository constructor() {

    private val _question: MutableStateFlow<List<QuestionDTO>> = MutableStateFlow(emptyList())
    val question: StateFlow<List<QuestionDTO>> = _question


    @OptIn(ExperimentalResourceApi::class)
    suspend fun loadQuestions() {
        val questions = withContext(Dispatchers.IO) {
            runCatching {
                val json = Res.readBytes("files/questions.json")
                val jsonString = json.decodeToString()
                println(jsonString)
                Json.decodeFromString<QuestionsDTO>(jsonString)

            }.getOrThrow()

        }
          _question.emit(questions.questions)
    }
}