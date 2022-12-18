package com.bestDate.data.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class LocationAutocompleteUtil(val context: Context) {
    private val token = AutocompleteSessionToken.newInstance()
    private val client: PlacesClient

    private var _locationsList: MutableLiveData<MutableList<CityListItem>> = MutableLiveData()
    var locationList: LiveData<MutableList<CityListItem>> = _locationsList

    init {
        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyCAvbkXsVq1FQuiLdwbwJxiM6WoYfYSX8I")
        }
        client = Places.createClient(context)
    }

    fun search(searchString: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.CITIES)
            .setSessionToken(token)
            .setQuery(searchString)
            .build()

        client.findAutocompletePredictions(request)
            .addOnSuccessListener {
                fetchResults(it, searchString)
            }
            .addOnFailureListener {
                Logger.print("Place autocomplete error: ${it.message}")
            }
    }

    private fun fetchResults(response: FindAutocompletePredictionsResponse, searchString: String) {
        val list: MutableList<CityListItem> = mutableListOf()

        response.autocompletePredictions.forEachIndexed { index, prediction ->
            val city = prediction.getPrimaryText(null).toString()
            val country = getItemFromLine(prediction.getSecondaryText(null).toString())
            val result = CityListItem(index, country, city)
            if (list.firstOrNull { it.city == result.city && it.country == result.country } == null &&
                result.getLocation() != searchString) {
                list.add(result)
            }
        }

        _locationsList.value = list
    }

    private fun getItemFromLine(line: String?): String {
        return if (line?.contains(",") == true)
            line.split(", ").lastOrNull().orEmpty()
        else line.orEmpty()
    }
}

data class CityListItem(
    var id: Int,
    var country: String?,
    var city: String
) {
    fun getLocation(): String {
        return "$city, ${country.orEmpty()}"
    }
}