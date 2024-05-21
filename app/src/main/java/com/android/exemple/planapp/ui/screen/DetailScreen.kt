package com.android.exemple.planapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.exemple.planapp.R
import com.android.exemple.planapp.ui.components.BottomBar
import com.android.exemple.planapp.ui.components.DetailList
import com.android.exemple.planapp.ui.theme.LocalThemeColors
import com.android.exemple.planapp.ui.viewmodels.DetailViewModel
import com.android.exemple.planapp.ui.viewmodels.PlanViewModel

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavController,
    planId: Int,
    modifier: Modifier = Modifier
) {
    viewModel.event(DetailViewModel.Event.Init(planId = planId))

    val themeColors = LocalThemeColors.current
    val uiState by viewModel.uiState.collectAsState()
    var launched by rememberSaveable { mutableStateOf(false) }
    if (launched.not()) {
        LaunchedEffect(Unit) {
            viewModel.event(DetailViewModel.Event.Init(planId = planId))
            launched = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_detail_plan)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home")
                    }) {
                        Icon(Icons.Filled.ArrowBack, stringResource(R.string.desc_back))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("detailCreate/${planId}") }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.desc_create)
                )
            }
        },
        bottomBar = {
            BottomBar(navController = navController, planId = planId)
        }
    ) {
        Column(modifier = modifier) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .background(themeColors.backgroundColor)
                    .padding(5.dp),
                fontSize = 22.sp,
                color = themeColors.textColor,
                text = stringResource(R.string.label_plan_list),
            )
            val details = uiState.details
            if (details != null) {
                DetailList(
                    details = details,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}


