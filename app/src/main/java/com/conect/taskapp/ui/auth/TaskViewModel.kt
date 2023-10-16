package com.conect.taskapp.ui.auth

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.conect.taskapp.R
import com.conect.taskapp.data.Task
import com.conect.taskapp.util.FirebaseHelper
import com.conect.taskapp.util.showBottonSheet

class TaskViewModel : ViewModel() {

    private val _taskInsert = MutableLiveData<Task>()
    val taskInsert: LiveData<Task> = _taskInsert

    private val _taskUpdate = MutableLiveData<Task>()
    val taskUpdate: LiveData<Task> = _taskUpdate

    fun setUpdateTask(task: Task) {
        _taskUpdate.postValue(task)
    }

    fun insertTask(task: Task) {
        FirebaseHelper.getDataBase()
            .child("Tasks")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    _taskInsert.postValue(task)
                    }
                }
    }
}