package com.android.exemple.planapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.exemple.planapp.ui.viewModel.PropertyViewModel

@Composable
fun PropertyCreateScreen(
    viewModel: PropertyViewModel = hiltViewModel(),
    navController: NavController,
    planId: Int
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.event(PropertyViewModel.Event.CreateInit(planId = planId))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "持ち物リスト作成") } ,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.createProperty()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.Add, null)
                    }
                }
            )
        }
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .background(
                        color = Color(0xffcccccc)
                    ),
                text = "持ち物",
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 5.dp, end = 5.dp),
                value = uiState.title,
                onValueChange = {
                    viewModel.event(PropertyViewModel.Event.TitleChanged(it))
                },
                label = { Text("例:水筒") }
            )
        }
    }
}