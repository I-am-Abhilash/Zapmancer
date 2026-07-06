package com.smach.zapmancer.features.projects.screen

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
import com.smach.zapmancer.features.projects.viewmodel.ProjectDetailEvent
import com.smach.zapmancer.features.projects.viewmodel.ProjectDetailViewModel
import com.smach.zapmancer.features.projects.viewmodel.ProjectListViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ProjectsAdaptiveScreen(
    listViewModel: ProjectListViewModel = koinViewModel(),
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
                ProjectListContent(
                    state = listState,
                    onEvent = listViewModel::onEvent,
                    onProjectClick = { id ->
                        scope.launch {
                            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id.toString())
                        }
                    },
                    onSearchClick = { },
                    showTopBar = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] != PaneAdaptedValue.Expanded,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let { projectId ->
                    val detailViewModel: ProjectDetailViewModel = koinViewModel(
                        key = projectId,
                        parameters = { parametersOf(projectId) },
                    )
                    val detailState by detailViewModel.uiState.collectAsState()
                    ProjectDetailContent(
                        state = detailState,
                        onBackClick = {
                            scope.launch {
                                scaffoldNavigator.navigateBack()
                            }
                        },
                        onSaveClick = { detailViewModel.onEvent(ProjectDetailEvent.ToggleSave) },
                        onApplyClick = { detailViewModel.onEvent(ProjectDetailEvent.Apply) },
                        showTopBar = true,
                    )
                }
            }
        },
    )
}
