package com.github.dimsmith.auraparfum.firebase

sealed class FirebaseResponse<out T> {
    class Success<T>(val data: T) :
            FirebaseResponse<T>()

    class Error(val exception: Exception) :
            FirebaseResponse<Exception>()
}