package com.bestDate.presentation.main.guests

import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.*
import com.bestDate.network.remote.GuestsRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuestsUseCase @Inject constructor(
    private val guestsRemoteData: GuestsRemoteData
) {
    private var originalList: MutableList<Guest>? = mutableListOf()
    var guestsList: MutableLiveData<MutableList<Guest>?> = MutableLiveData()
    var hasNewGuests: MutableLiveData<Boolean> = MutableLiveData(false)
    var meta: Meta = Meta()

    suspend fun getGuestsList() {
        val response = guestsRemoteData.getGuestsList(0)
        if (response.isSuccessful) {
            response.body()?.let {
                originalList = it.data
                meta = it.meta ?: Meta()
                transformGuestsList(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun getNextPage() {
        val page = meta.current_page.orZero + 1

        val response = guestsRemoteData.getGuestsList(page)
        if (response.isSuccessful) {
            response.body()?.let {
                originalList?.addAll(it.data.orEmpty())
                meta = it.meta ?: Meta()
                transformGuestsList(originalList)
            }
        }
    }

    private fun transformGuestsList(list: MutableList<Guest>?) {
        val newList: MutableList<Guest> = mutableListOf(createNewHeader())
        val oldList: MutableList<Guest> = mutableListOf(createOldHeader())
        list?.forEach {
            if (it.viewed != true) {
                newList.add(it.copy(itemType = ListItemType.NEW_ITEM))
            } else {
                oldList.add(it.copy(itemType = ListItemType.OLD_ITEM))
            }
        }
        hasNewGuests.postValue(newList.size > 1)

        when {
            newList.size > 1 && oldList.size > 1 -> {
                newList.addAll(oldList)
                guestsList.postValue(newList)
            }
            newList.size == 1 && oldList.size > 1 -> {
                guestsList.postValue(oldList)
            }
            newList.size > 1 && oldList.size == 1 -> {
                guestsList.postValue(newList)
            }
        }
    }

    private fun createNewHeader(): Guest =
        Guest(
            id = -2,
            guest = ShortUserData(id = R.string.new_guests),
            itemType = ListItemType.HEADER,
            viewed = true
        )

    private fun createOldHeader(): Guest =
        Guest(
            id = -1,
            guest = ShortUserData(id = R.string.prev_guests),
            itemType = ListItemType.HEADER,
            viewed = true
        )

    suspend fun markGuestsViewed(body: IdListRequest) {
        val response = guestsRemoteData.markGuestsViewed(body)
        if (!response.isSuccessful)
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        hasNewGuests.postValue(false)
        guestsList.postValue(null)
    }
}