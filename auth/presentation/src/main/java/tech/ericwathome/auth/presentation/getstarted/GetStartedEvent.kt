package tech.ericwathome.auth.presentation.getstarted

sealed interface GetStartedEvent {
    data object OnSuccess : GetStartedEvent
}