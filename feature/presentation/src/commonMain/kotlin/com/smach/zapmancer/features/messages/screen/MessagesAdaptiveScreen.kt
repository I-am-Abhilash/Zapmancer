package com.smach.zapmancer.features.messages.screen

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.smach.zapmancer.features.messages.viewmodel.MessagesDetailViewModel
import com.smach.zapmancer.features.messages.viewmodel.MessagesListViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Adaptive two-pane messages layout.
 *
 * On compact screens the list and detail panes are shown in sequence (like a
 * bottom-nav flow). On medium/expanded the [ListDetailPaneScaffold] shows both
 * side-by-side.
 *
 * Contact metadata (name, avatar, online status) is read from the list state when the
 * user taps a conversation and passed directly into [MessagesDetailViewModel] — no
 * extra network round-trip needed.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MessagesAdaptiveScreen(
    listViewModel: MessagesListViewModel = koinViewModel(),
    onProfileClick: (String) -> Unit = {},
    showSnackbar: (String) -> Unit = {},
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<String>()
    val listState by listViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = scaffoldNavigator.scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                MessagesListContent(
                    state = listState,
                    onEvent = listViewModel::onEvent,
                    onConversationClick = { id ->
                        scope.launch {
                            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                        }
                    },
                    onProfileClick = onProfileClick,
                    showTopBar = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] != PaneAdaptedValue.Expanded,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let { conversationId ->
                    // Find the conversation item to pass contact metadata to the ViewModel.
                    val conversation = listState.conversations.find { it.id == conversationId }
                    val contactName = conversation?.name.orEmpty()
                    val contactAvatarUrl = conversation?.avatarUrl.orEmpty()
                    val isOnline = conversation?.isOnline ?: false

                    val detailViewModel: MessagesDetailViewModel = koinViewModel(
                        key = conversationId,
                        parameters = { parametersOf(conversationId, contactName, contactAvatarUrl, isOnline) },
                    )
                    val detailState by detailViewModel.uiState.collectAsState()
                    MessageDetailContent(
                        state = detailState,
                        onEvent = detailViewModel::onEvent,
                        onBackClick = {
                            scope.launch {
                                scaffoldNavigator.navigateBack()
                            }
                        },
                        onProfileClick = onProfileClick,
                        onCallClick = { showSnackbar("Voice calling is not supported") },
                        onVideocamClick = { showSnackbar("Video calling is not supported") },
                        onMoreClick = { showSnackbar("More actions coming soon") },
                        showTopBar = true,
                    )
                }
            }
        },
    )
}
