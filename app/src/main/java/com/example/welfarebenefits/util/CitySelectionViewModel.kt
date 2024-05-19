package com.example.welfarebenefits.util

import androidx.lifecycle.ViewModel

class CitySelectionViewModel : ViewModel() {
    private var selectedCity: String? = null

    fun setSelectedCity(city: String) {
        selectedCity = city
    }

    fun getSelectedCity(): String? {
        return selectedCity
    }
}
