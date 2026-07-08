//package com.smach.zapmancer.features.messages.screen
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.smach.zapmancer.features.common.components.UserAvatar
//import com.smach.zapmancer.features.messages.state.MessagesListUiState
//import com.smach.zapmancer.features.messages.viewmodel.MessagesListEvent
//
//@Composable
//fun MessagesListContent(
//    state: MessagesListUiState,
//    onEvent: (MessagesListEvent) -> Unit,
//    onConversationClick: (String) -> Unit,
//    onProfileClick: (String) -> Unit = {},
//    showTopBar: Boolean = true,
//) {
//    val focusManager = LocalFocusManager.current
//    LaunchedEffect(Unit) { focusManager.clearFocus() }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        if (showTopBar) {
//            TopAppBar(
//                onProfileClick = onProfileClick,
//                onSearchChange = { query -> onEvent(MessagesListEvent.OnSearchQueryChanged(query)) },
//                onFilterChange = { filter -> onEvent(MessagesListEvent.OnFilterSelected(filter)) },
//                searchQuery = state.searchQuery,
//                selectedFilter = state.selectedFilter,
//            )
//        }
//        MessagesListPane(
//            state = state,
//            onEvent = onEvent,
//            onConversationClick = onConversationClick,
//        )
//    }
//}
//
//@Composable
//fun TopAppBar(
//    onProfileClick: (String) -> Unit,
//    onSearchChange: (String) -> Unit,
//    onFilterChange: (String) -> Unit,
//    searchQuery: String,
//    selectedFilter: String,
//) {
//    Surface(
//        color = MaterialTheme.colorScheme.surface,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp),
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = "Zapmancer",
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp,
//                color = MaterialTheme.colorScheme.primary,
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//            ) {
//                OutlinedTextField(
//                    value = searchQuery,
//                    onValueChange = onSearchChange,
//                    label = { Text("Search") },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Search,
//                            contentDescription = null,
//                            modifier = Modifier.size(24.dp),
//                        )
//                    },
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//                        focusedBorderColor = MaterialTheme.colorScheme.primary,
//                        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//                    ),
//                    modifier = Modifier
//                        .width(180.dp)
//                        .height(36.dp),
//                )
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                ) {
//                    listOf("All", "Unread", "Archived").forEach { filter ->
//                        val isSelected = filter == selectedFilter
//                        Surface(
//                            onClick = { onFilterChange(filter) },
//                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
//                            contentColor = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
//                            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
//                            modifier = Modifier.size(height = 32.dp, width = 56.dp),
//                        ) {
//                            Text(
//                                text = filter,
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            UserAvatar(
//                onClick = { onProfileClick("me") },
//                imageUrl = null,
//                size = 32.dp,
//                shape = MaterialTheme.shapes.extraLarge,
//                borderWidth = 1.dp,
//                borderColor = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.padding(end = 4.dp),
//            )
//        }
//    }
//}
