package com.conect.taskapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.conect.taskapp.R
import com.conect.taskapp.data.Status
import com.conect.taskapp.data.Task
import com.conect.taskapp.databinding.FragmentDoneBinding
import com.conect.taskapp.ui.adapter.TaskAdapter
import com.conect.taskapp.ui.auth.TaskViewModel
import com.conect.taskapp.util.FirebaseHelper
import com.conect.taskapp.util.showBottonSheet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DoneFragment : Fragment() {

    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initRecyclerView()
        getTasks()
    }

    private fun initRecyclerView() {
        taskAdapter = TaskAdapter(requireContext()) { task, option ->
            optionSelected(task, option)
        }

        with(binding.rvTasks) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            TaskAdapter.SELECT_BACK -> {
                task.status = Status.DOING
                updadeTask(task)
            }

            TaskAdapter.SELECT_REMOVE -> {
                showBottonSheet(
                    titleDialog = R.string.text_title_remove,
                    message = getString(R.string.text_message_confirm),
                    titleButton = R.string.text_button_remove,
                    onClick = {
                        deleteTask(task)
                    }
                )
            }

            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }

            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getTasks() {
        FirebaseHelper.getDataBase()
            .child("Tasks")
            .child(FirebaseHelper.getIdUser())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val taskList = mutableListOf<Task>()
                    for (ds in snapshot.children) {
                        val task = ds.getValue(Task::class.java) as Task
                        if (task.status == Status.DONE) {
                            taskList.add(task)
                        }
                    }

                    binding.progressBar.isVisible = false
                    listEmpty(taskList)
                    taskList.reverse()
                    taskAdapter.submitList(taskList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), R.string.erro_generic, Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun updadeTask(task: Task) {
        FirebaseHelper.getDataBase()
            .child("Tasks")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        R.string.task_editando_tarefa_sucesso,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), R.string.erro_generic, Toast.LENGTH_SHORT)
                        .show()
                }

            }
    }

    private fun deleteTask(task: Task) {
        FirebaseHelper.getDataBase()
            .child("Tasks")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .removeValue().addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), R.string.delet_task, Toast.LENGTH_SHORT).show()

                    val oldList = taskAdapter.currentList
                    val newList = oldList.toMutableList().apply {
                        remove(task)
                    }

                    taskAdapter.submitList(
                        newList
                    )
                } else {
                    Log.i("INFOTESTE", "onCancelled: ")
                }

            }
    }

    private fun listEmpty(taskList: List<Task>) {
        binding.textInfo.text = if (taskList.isEmpty()) {
            getString(R.string.text_list_task_empty)
        } else {
            ""
        }
    }

    private fun observeViewModel() {
        viewModel.taskUpdate.observe(viewLifecycleOwner) { updateTask ->
            if (updateTask.status == Status.DONE) {

                // Armazena a lista atual do Adapter
                val oldList = taskAdapter.currentList

                // Gera uma nova lista a partir da lista antiga já com a tarefa atualizada
                val newList = oldList.toMutableList().apply {
                    find { it.id == updateTask.id }?.description = updateTask.description
                }

                // Armazena a posicao da tarefa a ser atualizada na lista
                val position = newList.indexOfFirst { it.id == updateTask.id }

                // Envia a lista atualizada para o Adapter
                taskAdapter.submitList(newList)

                // Atualiza a tarefa pela posição do Adpater
                taskAdapter.notifyItemChanged(position)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}