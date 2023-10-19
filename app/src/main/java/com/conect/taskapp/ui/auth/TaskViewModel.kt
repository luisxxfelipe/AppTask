package com.conect.taskapp.ui.auth

import android.system.Os.remove
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.conect.taskapp.R
import com.conect.taskapp.data.Status
import com.conect.taskapp.data.Task
import com.conect.taskapp.util.FirebaseHelper
import com.conect.taskapp.util.StateView
import com.conect.taskapp.util.showBottonSheet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TaskViewModel : ViewModel() {
    private val _taskList = MutableLiveData<StateView<List<Task>>>()
    val taskList: LiveData<StateView<List<Task>>> = _taskList

    private val _taskInsert = MutableLiveData<Task>()
    val taskInsert: LiveData<Task> = _taskInsert

    private val _taskUpdate = MutableLiveData<Task>()
    val taskUpdate: LiveData<Task> = _taskUpdate

    private val _taskDelete = MutableLiveData<Task>()
    val taskDelete: LiveData<Task> = _taskDelete
    fun getTasks(status: Status) {
        try{

            _taskList.postValue(StateView.OnLoading())
            FirebaseHelper.getDataBase()
                .child("Tasks")
                .child(FirebaseHelper.getIdUser())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val taskList = mutableListOf<Task>()
                        for (ds in snapshot.children) {
                            val task = ds.getValue(Task::class.java) as Task
                            if (task.status == status) {
                                taskList.add(task)
                            }
                        }
                        taskList.reverse()
                        _taskList.postValue(StateView.OnSucess(taskList))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.i("INFOTESTE", "onCancelled: ")
                    }

                })
        }catch (ex: Exception){
            _taskList.postValue(StateView.OnError(ex.message.toString()))
        }
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

    fun updateTask(task: Task){
        val map = mapOf(
            "description" to task.description,
            "status" to task.status
        )

        FirebaseHelper.getDataBase()
            .child("Tasks")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .updateChildren(map)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    _taskUpdate.postValue(task)
                }
            }
    }

    fun deleteTask(task: Task) {
        FirebaseHelper.getDataBase()
            .child("Tasks")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .removeValue().addOnCompleteListener { result ->
                if (result.isSuccessful) {
                  _taskDelete.postValue(task)
                }

            }
    }
}