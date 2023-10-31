package com.conect.taskapp.util

sealed class StateView<T>(val data: T? = null, val massage: String? = null) {
    class OnLoading<T>: StateView<T>()
    class OnSuccess<T>(data: T, massage: String? = null): StateView<T>(data, massage)
    class OnError<T>(massage: String? = null): StateView<T>(null, massage)
}

