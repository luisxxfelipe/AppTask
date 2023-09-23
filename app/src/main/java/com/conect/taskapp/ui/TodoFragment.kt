package com.conect.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.conect.taskapp.R
import com.conect.taskapp.data.Status
import com.conect.taskapp.data.Task
import com.conect.taskapp.databinding.FragmentTodoBinding
import com.conect.taskapp.ui.adapter.TaskAdapter
import com.conect.taskapp.ui.adapter.TaskTopAdapter

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskTopAdapter: TaskTopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initRecyclerView()
        getTasks()
    }

    private fun initListener() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
        }
    }

    private fun initRecyclerView() {
        taskTopAdapter = TaskTopAdapter { task, option ->
            optionSelected(task, option)}

        taskAdapter = TaskAdapter(requireContext()) { task, option ->
            optionSelected(task, option)}

        val concatAdapter = ConcatAdapter(taskTopAdapter, taskAdapter)

        with(binding.rvTasks){
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = concatAdapter
        }
    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            TaskAdapter.SELECT_REMOVE -> {
                Toast.makeText(
                    requireContext(),
                    "Removendo ${task.description}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            TaskAdapter.SELECT_EDIT -> {
                Toast.makeText(requireContext(), "Editando ${task.description}", Toast.LENGTH_SHORT)
                    .show()
            }

            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT)
                    .show()
            }

            TaskAdapter.SELECT_NEXT -> {
                Toast.makeText(requireContext(), "Próximo ${task.description}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getTasks() {
        val taskTopList = listOf(
            Task("0", "Essa Tarefa está no topo da minha lista", Status.TODO)
        )

        val taskList = listOf(
            Task("0", "Fazer a atividade de EDO", Status.TODO),
            Task("1", "Ir na academia as 16h", Status.TODO),
            Task("2", "Continuar o curso de Kotlin", Status.TODO),
            Task("3", "Assistir jogo do corinthians", Status.TODO),
            Task("4", "Fazer a atividade de circuitos", Status.TODO)
        )

        taskTopAdapter.submitList(taskTopList)
        taskAdapter.submitList(taskList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}