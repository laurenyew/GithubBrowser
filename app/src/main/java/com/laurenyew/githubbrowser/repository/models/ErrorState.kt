package com.laurenyew.githubbrowser.repository.models

sealed class ErrorState {
    object NetworkError : ErrorState()
    object MalformedResultError : ErrorState()
    data class UnknownError(val errorMessage: String?) : ErrorState()
}