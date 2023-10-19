package com.conect.taskapp.util

import java.lang.Thread.State

sealed class StateView<T>(val data: T? = null, val message: String? = null){
    class OnLoading<T>: StateView<T>()
    class OnSucess<T>(data: T, message: String? = null): StateView<T>(data, message)
    class OnError<T>(message: String? = null): StateView<T>(null, message)


}
