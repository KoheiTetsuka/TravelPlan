package com.android.exemple.planapp.ui.screen

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.exemple.planapp.R
import com.android.exemple.planapp.ui.viewmodels.PlanViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun PlanCreateScreen(
    viewModel: PlanViewModel = hiltViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat =
        DateTimeFormatter.ofPattern(stringResource(R.string.format_yyyy_mm_dd_e), Locale.JAPAN)
    val focusRequesterStartDate = remember { FocusRequester() }
    val focusRequesterEndDate = remember { FocusRequester() }

    var hasTitleError: Boolean = uiState.titleErrorMessage.isNotEmpty()
    var hasDateError: Boolean = uiState.dateErrorMessage.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_travel_plan_create)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.event(PlanViewModel.Event.Init)
                    }) {
                        Icon(Icons.Filled.ArrowBack, stringResource(R.string.desc_back))
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (!hasTitleError && !hasDateError) {
                            viewModel.event(PlanViewModel.Event.OnCreatePlanClicked(uiState))
                        }
                    }) {
                        Icon(Icons.Filled.Add, stringResource(R.string.desc_create))
                    }
                },
            )
            // 登録が完了すれば、前画面に遷移する
            if (uiState.popBackStackFlag) {
                navController.navigate("home")
                viewModel.initializePopBackStackFlag()
            }
        }
    ) {
        Column {
            Row {
                Text(
                    modifier = Modifier
                        .background(
                            color = Color(0xffcccccc)
                        ),
                    text = stringResource(R.string.label_title),
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .background(
                            color = Color(0xffcccccc)
                        ),
                    text = stringResource(R.string.label_required),
                    color = Color.Red
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 5.dp, end = 5.dp),
                value = uiState.title,
                onValueChange = {
                    viewModel.event(PlanViewModel.Event.TitleChanged(it))
                },
                label = { Text(stringResource(R.string.label_okinawa)) },
                singleLine = true,
                isError = uiState.titleErrorMessage.isNotEmpty(),
                trailingIcon = {
                    if (uiState.titleErrorMessage.isEmpty()) return@OutlinedTextField
                    Icon(
                        Icons.Filled.Error,
                        stringResource(R.string.desc_error),
                        tint = MaterialTheme.colors.error
                    )
                },
            )
            if (uiState.titleErrorMessage.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.error_title),
                    color = MaterialTheme.colors.error
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .background(
                        color = Color(0xffcccccc)
                    ),
                text = stringResource(R.string.label_detail),
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 5.dp, end = 5.dp),
                value = uiState.description,
                onValueChange = {
                    viewModel.event(PlanViewModel.Event.DescriptionChanged(it))
                },
                label = { Text(stringResource(R.string.label_graduation)) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .background(
                        color = Color(0xffcccccc)
                    ),
                text = stringResource(R.string.label_start_day),
            )
            Row(
                modifier = Modifier
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequesterStartDate),
                    value = if (uiState.startDate == null) stringResource(R.string.empty) else dateFormat.format(
                        uiState.startDate
                    ),
                    onValueChange = {},
                    readOnly = true,
                    isError = uiState.dateErrorMessage.isNotEmpty(),
                    trailingIcon = {
                        if (uiState.dateErrorMessage.isEmpty()) return@OutlinedTextField
                        Icon(
                            Icons.Filled.Error,
                            stringResource(R.string.desc_error),
                            tint = MaterialTheme.colors.error
                        )
                    },
                )
                Spacer(modifier = Modifier.width(15.dp))
                IconButton(
                    modifier = Modifier.width(30.dp),
                    onClick = {
                        showDatePicker(
                            context,
                            onDecideDate = { date ->
                                viewModel.event(PlanViewModel.Event.StartDateChanged(date))
                            }
                        )
                        focusRequesterStartDate.requestFocus()
                    }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.desc_error)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .background(
                        color = Color(0xffcccccc)
                    ),
                text = stringResource(R.string.label_end_day),
            )
            Row(
                modifier = Modifier
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequesterEndDate),
                    value = if (uiState.endDate == null) stringResource(R.string.empty) else dateFormat.format(
                        uiState.endDate
                    ),
                    onValueChange = {},
                    readOnly = true,
                    isError = uiState.dateErrorMessage.isNotEmpty(),
                    trailingIcon = {
                        if (uiState.dateErrorMessage.isEmpty()) return@OutlinedTextField
                        Icon(
                            Icons.Filled.Error,
                            stringResource(R.string.desc_error),
                            tint = MaterialTheme.colors.error
                        )
                    },
                )
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(
                    modifier = Modifier.width(30.dp),
                    onClick = {
                        showDatePicker(
                            context,
                            onDecideDate = { date ->
                                viewModel.event(PlanViewModel.Event.EndDateChanged(date))
                            }
                        )
                        focusRequesterEndDate.requestFocus()
                    }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.desc_end_day)
                    )
                }
            }
            if (uiState.dateErrorMessage.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.error_date_validate),
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}

private fun showDatePicker(
    context: Context,
    onDecideDate: (LocalDate) -> Unit,
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    DatePickerDialog(
        context,
        { _: DatePicker, pickedYear: Int, pickedMonth: Int, pickedDay: Int ->
            onDecideDate(
                LocalDate.of(pickedYear, pickedMonth + 1, pickedDay)
            )
        }, year, month, day
    ).show()
}
