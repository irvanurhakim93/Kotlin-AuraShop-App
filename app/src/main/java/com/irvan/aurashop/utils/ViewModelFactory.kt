package com.github.dimsmith.auraparfum.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory<T : ViewModel>(private val instance: T, application: Application) :
        AndroidViewModel(application) {
    fun getViewModel(): ViewModelProvider.NewInstanceFactory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return instance as T
            }
        }
    }
}