package com.example.featurefallingwords.viewmodel

import com.example.core.viewmodel.ViewEffect
import com.example.core.viewmodel.ViewEvent
import com.example.core.viewmodel.ViewState
import com.example.featurefallingwords.model.WordUiModel

object GameContract {
    data class State(
        val points: Int = 0,
        val lives: Int = 3,
        val wordUiModel: WordUiModel? = null
    ) : ViewState

    sealed class Event : ViewEvent {
        object OnInitViewModel : Event()
        object OnCorrectClicked : Event()
        object OnWrongClicked : Event()
        object OnWordFallen : Event()
    }

    sealed class Effect : ViewEffect {
        data class UnknownErrorEffect(val message: String) : Effect()
        object OnGameFinished : Effect()
    }
}
