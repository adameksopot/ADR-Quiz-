package org.example.project.screens.welcome

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.example.project.data.QuestionRepository


class WelcomeViewModel (
    private val questionRepository: QuestionRepository
) : StateScreenModel<WelcomeViewModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data object Success : State()
        data object Error: State()

    }

    fun loadQuestions() {
        screenModelScope.launch(Dispatchers.IO) {
            try {
                mutableState.value = State.Success
                questionRepository.loadQuestions()
            } catch (ex: Exception) {
                mutableState.value = State.Error

            }
        }
    }
}