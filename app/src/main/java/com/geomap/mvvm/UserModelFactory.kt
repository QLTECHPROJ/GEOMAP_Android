package com.geomap.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserModelFactory constructor(private val repository : UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        return if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            ViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}