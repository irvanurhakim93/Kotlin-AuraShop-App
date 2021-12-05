package com.github.dimsmith.auraparfum.firebase

interface FirebaseListener<in T> {
    fun onCompletedFetch(items: List<T>)
    fun onError(exception: Exception)
}