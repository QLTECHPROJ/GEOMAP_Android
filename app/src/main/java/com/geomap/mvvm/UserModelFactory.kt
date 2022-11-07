package com.geomap.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserModelFactory constructor(private val repository : UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        return if (modelClass.isAssignableFrom(AllViewModel::class.java)) {
            AllViewModel(repository) as T
        } else {
            throw IllegalArgumentException("AllViewModel Not Found")
        }
    }
}