package com.conect.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.conect.taskapp.R
import com.conect.taskapp.data.Status
import com.conect.taskapp.data.Task
import com.conect.taskapp.databinding.FragmentTodoBinding
import com.conect.taskapp.ui.adapter.TaskAdapter
import com.conect.taskapp.ui.auth.TaskViewModel
import com.conect.taskapp.util.StateView
import com.conect.taskapp.util.showBottonSheet

class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        initRecyclerView()

        observeViewModel()

        viewModel.getTasks()
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewModel.taskList.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.OnSuccess -> {

                    val taskList = stateView.data?.filter { it.status == Status.TODO }

                    binding.progressBar.isVisible = false
                    listEmpty(taskList ?: emptyList())

                    taskAdapter.submitList(taskList)
                }

                is StateView.OnError -> {
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }
        }

        viewModel.taskInsert.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false

                    if (stateView.data?.status == Status.TODO) {
                        // Armazena a lista atual do adapter
                        val oldList = taskAdapter.currentList

                        // Gera uma nova lista a partir da lista antiga já com a tarefa atualizada
                        val newList = oldList.toMutableList().apply {
                            add(0, stateView.data)
                        }

                        // Envia a lista atualizada para o adapter
                        taskAdapter.submitList(newList)

                        setPositionRecyclerView()
                    }

                }

                is StateView.OnError -> {
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }
        }

        viewModel.taskUpdate.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false

                    // Armazena a lista atual do adapter
                    val oldList = taskAdapter.currentList

                    // Gera uma nova lista a partir da lista antiga já com a tarefa atualizada
                    val newList = oldList.toMutableList().apply {
                        if (!oldList.contains(stateView.data) && stateView.data?.status == Status.TODO) {
                            add(0, stateView.data)
                            setPositionRecyclerView()
                        }

                        if (stateView.data?.status == Status.TODO) {
                            find { it.id == stateView.data.id }?.description =
                                stateView.data.description
                        } else {
                            remove(stateView.data)
                        }
                    }

                    // Armazena a posição da tarefa a ser atualizada na lista
                    val position = newList.indexOfFirst { it.id == stateView.data?.id }

                    // Envia a lista atualizada para o adapter
                    taskAdapter.submitList(newList)

                    // Atualiza a tarefa pela posição do adapter
                    taskAdapter.notifyItemChanged(position)
                }

                is StateView.OnError -> {
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }
        }

        viewModel.taskDelete.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.OnSuccess -> {
                    binding.progressBar.isVisible = false

                    Toast.makeText(
                        requireContext(),
                        R.string.delet_task,
                        Toast.LENGTH_SHORT
                    ).show()

                    val oldList = taskAdapter.currentList
                    val newList = oldList.toMutableList().apply {
                        remove(stateView.data)
                    }
                    taskAdapter.submitList(newList)
                }

                is StateView.OnError -> {
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }
        }
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
            TaskAdapter.SELECT_REMOVE -> {
                showBottonSheet(
                    titleDialog = R.string.text_title_remove,
                    message = getString(R.string.text_message_confirm),
                    titleButton = R.string.text_button_remove,
                    onClick = {
                        viewModel.deleteTask(task)
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

            TaskAdapter.SELECT_NEXT -> {
                task.status = Status.DONE
                viewModel.updateTask(task)
            }
        }
    }

    private fun setPositionRecyclerView() {
        taskAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {

            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {

            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {

            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvTasks.scrollToPosition(0)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {

            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {

            }
        })
    }

    private fun listEmpty(taskList: List<Task>) {
        binding.textInfo.text = if (taskList.isEmpty()) {
            getString(R.string.text_list_task_empty)
        } else {
            ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}