package com.android.exemple.planapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.exemple.planapp.db.entities.Plan
import com.android.exemple.planapp.ui.repository.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val planRepository: PlanRepository
) : ViewModel() {

    val plans = planRepository.getAll()

    data class UiState(
        val title: String = "",
        val description: String = "",
        val startDate: LocalDate? = null,
        val endDate: LocalDate? = null,
        val titleErrorMessage: String = "",
        val dateErrorMessage: String = "",
        val startDateErrorMessage: String = "",
        val endDateErrorMessage: String = "",
        val popBackStackFlag: Boolean = false,
    )

    sealed class Event {
        data class EditInit(val planId: Int) : Event()
        data class TitleChanged(val title: String) : Event()
        data class DescriptionChanged(val description: String) : Event()
        data class StartDateChanged(val startDate: LocalDate) : Event()
        data class EndDateChanged(val endDate: LocalDate) : Event()
        data class OnCreatePlanClicked(val uiState: UiState) : Event()
        data class OnUpdatePlanClicked(val uiState: UiState, val planId: Int) : Event()
        data class OnDeletePlanClicked(val plan: Plan) : Event()
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun event(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.EditInit -> {
                    val plan = planRepository.getById(event.planId)
                    _uiState.update {
                        it.copy(
                            title = plan.title,
                            description = plan.description,
                            startDate = plan.startDate,
                            endDate = plan.endDate
                        )
                    }
                }

                is Event.TitleChanged -> {
                    _uiState.update {
                        it.copy(title = event.title, titleErrorMessage = "")
                    }
                }

                is Event.DescriptionChanged -> {
                    _uiState.update {
                        it.copy(description = event.description)
                    }
                }

                is Event.StartDateChanged -> {
                    _uiState.update {
                        it.copy(startDate = event.startDate, dateErrorMessage = "")
                    }
                }

                is Event.EndDateChanged -> {
                    _uiState.update {
                        it.copy(endDate = event.endDate, dateErrorMessage = "")
                    }
                }

                is Event.OnCreatePlanClicked -> {
                    viewModelScope.launch {
                        if (_uiState.value.title.isEmpty()) {
                            _uiState.update {
                                it.copy(titleErrorMessage = "タイトルは必須です。")
                            }
                            return@launch
                        }
                        if (!checkDateValidate()) {
                            _uiState.update {
                                it.copy(dateErrorMessage = "終了日は開始日より後の日付を入力してください。")
                            }
                            return@launch
                        }
                        val newPlan = Plan(
                            title = _uiState.value.title,
                            description = _uiState.value.description,
                            startDate = _uiState.value.startDate,
                            endDate = _uiState.value.endDate
                        )

                        planRepository.insertPlan(newPlan)
                        _uiState.update {
                            it.copy(popBackStackFlag = true)
                        }
                    }
                }

                is Event.OnUpdatePlanClicked -> {
                    viewModelScope.launch {
                        if (_uiState.value.title.isEmpty()) {
                            _uiState.update {
                                it.copy(titleErrorMessage = "タイトルは必須です。")
                            }
                            return@launch
                        }

                        if (!checkDateValidate()) {
                            _uiState.update {
                                it.copy(dateErrorMessage = "終了日は開始日より後の日付を入力してください。")
                            }
                            return@launch
                        }
                        val newPlan = Plan(
                            id = event.planId,
                            title = _uiState.value.title,
                            description = _uiState.value.description,
                            startDate = _uiState.value.startDate,
                            endDate = _uiState.value.endDate
                        )
                        planRepository.updatePlan(newPlan)
                    }
                }

                is Event.OnDeletePlanClicked -> {
                    viewModelScope.launch {
                        planRepository.deletePlan(event.plan)
                    }
                }
            }
        }
    }

    /**
     * 開始日と終了日を比較する。
     */
    private fun checkDateValidate(): Boolean {
        val startDate = _uiState.value.startDate
        val endDate = _uiState.value.endDate

        // 開始日と終了日が同日でないかつ開始日が終了日より遅かった場合エラー
        return !(startDate?.isEqual(endDate) == false && !startDate.isBefore(endDate))
    }

    /**
     * popBackStackFlagを初期化する
     */
    fun initializePopBackStackFlag() {
        _uiState.update {
            it.copy(popBackStackFlag = false)
        }
    }
}