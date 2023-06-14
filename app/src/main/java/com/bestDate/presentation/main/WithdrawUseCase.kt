package com.bestDate.presentation.main

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.UserRemoteData
import com.bestDate.view.alerts.BuyDialogType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WithdrawUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    suspend fun withdrawCoins(type: BuyDialogType) {
        val response = userRemoteData.withdrawCoins(type)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }
}