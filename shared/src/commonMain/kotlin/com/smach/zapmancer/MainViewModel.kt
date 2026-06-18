package com.smach.zapmancer

//
///** Authentication session states */
//sealed interface SessionState {
//    data object Loading : SessionState
//    data object Unauthenticated : SessionState
//    data class Authenticated(val accessToken: String) : SessionState
//}
//
///** Central ViewModel that drives the UI */
//class MainViewModel(
//    private val authRepository: AuthRepository,
//) : ViewModel() {
//
//    val session: StateFlow<SessionState> = authRepository.getAccessToken()
//        .map { token ->
//            if (token.isNullOrBlank()) {
//                SessionState.Unauthenticated
//            } else {
//                SessionState.Authenticated(token)
//            }
//        }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = SessionState.Loading,
//        )
//
//    fun logout() {
//        viewModelScope.launch {
//            authRepository.logout()
//        }
//    }
//}
