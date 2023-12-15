package com.lahsuak.apps.geminiai.ui.model.states

/**
 * A sealed hierarchy describing the state of the text generation.
 */
sealed interface PhotoReasoningUiState {

    /**
     * Empty state when the screen is first shown
     */
    data object Initial: PhotoReasoningUiState

    /**
     * Still loading
     */
    data object Loading: PhotoReasoningUiState

    /**
     * Text has been generated
     */
    data class Success(
        val outputText: String
    ): PhotoReasoningUiState

    /**
     * There was an error generating text
     */
    data class Error(
        val errorMessage: String
    ): PhotoReasoningUiState
}
