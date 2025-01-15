package com.example.kebabapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class KebabDetailPageViewModel : ViewModel() {
    private val kebabId = MutableLiveData<Int>()

    // Setter for kebabId
    fun setKebabId(id: Int) {
        kebabId.value = id
    }

    // Getter for kebabId
    fun getKebabId(): Int? {
        return kebabId.value
    }
}