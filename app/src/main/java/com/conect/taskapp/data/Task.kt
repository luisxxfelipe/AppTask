package com.conect.taskapp.data

import android.os.Parcelable
import com.conect.taskapp.util.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var description: String = "",
    var status: Status = Status.TODO
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDataBase().push().key ?: ""
    }
}
