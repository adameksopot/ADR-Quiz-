package org.example.project.screens.asker

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.example.project.data.QuestionRepository

class AskerViewModel(
    private val questionRepository: QuestionRepository,
    private val questionMapper: QuestionMapper
) : ScreenModel {

    private val _uiState = MutableStateFlow(Questions.EMPTY)
    val uiState: StateFlow<Questions> = _uiState

    init {
        screenModelScope.launch {
            questionRepository.question.collectLatest { questions ->
                val questionStates = questions.mapIndexed { index, question ->
                    questionMapper.map(question, index, questions.size)
                }
                _uiState.value = Questions(questionStates)
            }
        }
    }
}
