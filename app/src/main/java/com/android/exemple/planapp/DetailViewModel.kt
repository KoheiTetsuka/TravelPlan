package com.android.exemple.planapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.exemple.planapp.db.dao.DetailDao
import com.android.exemple.planapp.db.entities.Detail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailDao: DetailDao) : ViewModel() {

    val details = detailDao.getAll().distinctUntilChanged()
    private var planId = -1
    fun setId(planId: Int) {
        this.planId = planId
    }



    data class UiState(
        val title: String = "",
        val cost: String = "",
        val url: String = "",
        val memo: String = "",
        val startTime: LocalTime? = null,
        val endTime: LocalTime? = null,
        val date: LocalDate? = null,
        val titleErrorMessage: String = "",
        val timeErrorMessage: String = "",
    )

    sealed class Event {
        data class TitleChanged(val title: String) : Event()
        data class CostChanged(val cost: String) : Event()
        data class UrlChanged(val url: String) : Event()
        data class MemoChanged(val memo: String) : Event()
        data class StartTimeChanged(val startTime: LocalTime) : Event()
        data class EndTimeChanged(val endTime: LocalTime) : Event()
        data class DateChanged(val date: LocalDate) : Event()
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun event(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.TitleChanged -> {
                    _uiState.update {
                        it.copy(title = event.title)
                    }
                }

                is Event.CostChanged -> {
                    _uiState.update {
                        it.copy(cost = event.cost)
                    }
                }

                is Event.UrlChanged -> {
                    _uiState.update {
                        it.copy(url = event.url)
                    }
                }

                is Event.MemoChanged -> {
                    _uiState.update {
                        it.copy(memo = event.memo)
                    }
                }

                is Event.StartTimeChanged -> {
                    _uiState.update {
                        it.copy(startTime = event.startTime)
                    }
                }

                is Event.EndTimeChanged -> {
                    _uiState.update {
                        it.copy(endTime = event.endTime)
                    }
                }

                is Event.DateChanged -> {
                    _uiState.update {
                        it.copy(date = event.date)
                    }
                }
            }
        }
    }

    fun createDetail() {
        viewModelScope.launch {
            if (_uiState.value.title.isEmpty()) {
                _uiState.update {
                    it.copy(titleErrorMessage = "タイトルは必須です。")
                }
                return@launch
            }

            if (!checkTimeValidate()) {
                _uiState.update {
                    it.copy(timeErrorMessage = "終了時間は開始時間より後の時刻を入力してください。")
                }
                return@launch
            }

            val newDetail = Detail(
                title = _uiState.value.title,
                cost = _uiState.value.cost,
                url = _uiState.value.url,
                memo = _uiState.value.memo,
                startTime = _uiState.value.startTime,
                endTime = _uiState.value.endTime,
                date = _uiState.value.date,
                planId = planId
            )

            detailDao.insertDetail(newDetail)
        }
    }


    fun deleteDetail(detail: Detail) {
        viewModelScope.launch {
            detailDao.deleteDetail(detail)
        }
    }

    /**
     * 開始時間と終了時間を比較する。
     * 終了時間より開始時間が遅かったらエラー
     */
    private fun checkTimeValidate(): Boolean {
        val startTime = _uiState.value.startTime
        val endTime = _uiState.value.endTime

        return startTime?.isBefore(endTime) != false
    }
}