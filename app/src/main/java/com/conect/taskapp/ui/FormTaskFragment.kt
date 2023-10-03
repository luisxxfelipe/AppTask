package com.conect.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.conect.taskapp.R
import com.conect.taskapp.data.Status
import com.conect.taskapp.data.Task
import com.conect.taskapp.databinding.FragmentFormTaskBinding
import com.conect.taskapp.databinding.FragmentRegisterBinding
import com.conect.taskapp.util.initToolBar
import com.conect.taskapp.util.showBottonSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var status: Status = Status.TODO
    private var newTask: Boolean = true
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    private val args: FormTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar)

        reference = Firebase.database.reference
        auth = Firebase.auth

        getArgs()
        initListener()
    }

    private fun getArgs(){
        args.task.let{
            if(it != null){
                this.task = it
                configTask()
            }
        }
    }

    private fun initListener(){
        binding.btnSave.setOnClickListener{
            validateData()

            binding.rgStatus.setOnCheckedChangeListener{_, id->
                status = when(id){
                    R.id.rbTodo -> Status.TODO
                    R.id.rbDoing -> Status.DOING
                    else -> Status.DOING
                }
            }

        }
    }

    private fun setStatus(){

        binding.rgStatus.check(
            when(task.status){
            Status.TODO -> R.id.rbTodo
            Status.DOING -> R.id.rbDoing
            else->R.id.rbDone
    })
    }

    private fun configTask(){

        newTask = false
        status = task.status
        binding.titleNovaTarefa.setText(R.string.nova_tarefa)
        binding.editDescription.setText(task.description)
        setStatus()
    }

    private fun validateData(){
        val description = binding.editDescription.text.toString().trim()

        if(description.isNotEmpty()){
            binding.progresbar.isVisible = true

            if(newTask) {
                task = Task()
                task.id = reference.database.reference.push().key ?: ""
            }
            task.description = description
            task.status = status

            saveTask()
        }else{
            showBottonSheet(message = getString(R.string.descricao_default_task))
        }
    }

    private fun saveTask(){
        reference.child("Tasks")
            .child(auth.currentUser?.uid?: "")
            .child(task.id)
            .setValue(task).addOnCompleteListener{result->
                if(result.isSuccessful){
                    Toast.makeText(requireContext(), R.string.task_save, Toast.LENGTH_SHORT
                    ).show()

                    if(newTask){ // sempre que o usuario tiver criando uma nova tarefa
                        findNavController().popBackStack()
                    }else{ // editando uma tarefa
                        binding.progresbar.isVisible = false
                    }
                }else{
                    binding.progresbar.isVisible = false
                    showBottonSheet(message = getString(R.string.erro_generic))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}