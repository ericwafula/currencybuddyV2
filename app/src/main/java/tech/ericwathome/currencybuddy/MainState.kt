package tech.ericwathome.currencybuddy

data class MainState(
    val isCheckingOnBoardingStatus: Boolean = false,
    val isOnboarded: Boolean = false,
    val showNetworkPopup: Boolean = false,
)
