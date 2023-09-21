package com.example.sweetstreet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sweetstreet.SweetEvent
import com.example.sweetstreet.ui.components.FilterDrawerSheet
import com.example.sweetstreet.ui.views.ListView
import com.example.sweetstreet.ui.views.MapView
import com.example.sweetstreet.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(viewModel: BaseViewModel) {

    val collectedUiState: State<UiState> = viewModel.uiState.collectAsState()

    val onEvent = viewModel::onEvent

    val onListView = collectedUiState.value.onListView

    // nav drawer and scope
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Dessert Filter",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // icon button to close drawer
                        IconButton(onClick = {
                            scope.launch { drawerState.close() }
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "close")
                        }
                    }
                    Divider(modifier = Modifier.padding(end = 12.dp))
                    FilterDrawerSheet(
                        uiState = collectedUiState,
                        onEvent = onEvent
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, top = 12.dp)
                    ) {
                        Column {
                            Button(
                                onClick = { scope.launch { drawerState.close() } }
                            ) {
                                Text(text = "Show results")
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(
                        text = "Sweet Street",
                        color = MaterialTheme.colorScheme.onPrimary
                    ) },
                    actions = {
                        if (onListView) {
                            IconButton(
                                // open drawer on click
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }) {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = "filter",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            }
        ) {paddingValues ->
            // set ab state and tab options
            var tabState = collectedUiState.value.tabIndex
            val titles = listOf("List view", "Map view")
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                // generate tab for each option in list
                TabRow(selectedTabIndex = tabState) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(text = title) },
                            selected = tabState == index,
                            // set tab state in ui state and within composable
                            onClick = {
                                onEvent(SweetEvent.SelectTab(tabState))

                                tabState = index

                            }
                        )
                    }
                }
                // load view composable depending on tab selection
                if (tabState == 0) {
                    ListView(
                        uiState = viewModel.uiState,
                        onEvent = onEvent
                    )
                } else {
                    MapView(
                        uiState = viewModel.uiState,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}