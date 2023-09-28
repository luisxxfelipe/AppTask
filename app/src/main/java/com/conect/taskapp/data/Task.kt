package com.conect.taskapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var description: String= "",
    var status: Status = Status.TODO
): Parcelable
