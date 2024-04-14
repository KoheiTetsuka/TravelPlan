package com.android.exemple.planapp.ui.repository

import com.android.exemple.planapp.db.entities.Detail
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    fun getAllById(planId: Int): Flow<MutableList<Detail>>
    fun getById(detailId: Int): Flow<Detail>
    suspend fun insertDetail(detail: Detail)
    suspend fun updateDetail(detail: Detail)
    suspend fun deleteDetail(detail: Detail)
    suspend fun deleteDetailByPlanId(planId: Int)
}