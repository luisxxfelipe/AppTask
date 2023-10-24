package com.conect.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.conect.taskapp.R
import com.conect.taskapp.data.Status
import com.conect.taskapp.data.Task
import com.conect.taskapp.databinding.FragmentFormTaskBinding
import com.conect.taskapp.ui.auth.TaskViewModel
import com.conect.taskapp.util.initToolBar
import com.conect.taskapp.util.showBottonSheet


class FormTaskFragment : BaseFragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var status: Status = Status.TODO
    private var newTask: Boolean = true

    private val args: FormTaskFragmentArgs by navArgs()

    private val viewModel: TaskViewModel by activityViewModels()

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

        getArgs()
        initListener()
    }

    private fun getArgs() {
        args.task.let {
            if (it != null) {
                this.task = it
                configTask()
            }
        }
    }

    private fun initListener() {
        binding.btnSave.setOnClickListener {
            observeViewModel()
            validateData()
        }

        binding.rgStatus.setOnCheckedChangeListener { _, id ->
            status = when (id) {
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun setStatus() {

        binding.rgStatus.check(
            when (task.status) {
                Status.TODO -> R.id.rbTodo
                Status.DOING -> R.id.rbDoing
                else -> R.id.rbDone
            }
        )
    }

    private fun configTask() {

        newTask = false
        status = task.status
        binding.titleNovaTarefa.setText(R.string.task_editando_tarefa)
        binding.editDescription.setText(task.description)
        setStatus()
    }

    private fun validateData() {
        val description = binding.editDescription.text.toString().trim()

        if (description.isNotEmpty()) {

            hideKeyboard()
            binding.progresbar.isVisible = true

            if (newTask) task = Task()
            task.description = description
            task.status = status

            if (newTask) {
                viewModel.insertTask(task)
            } else {
                viewModel.updateTask(task)
            }
        } else {
            showBottonSheet(message = getString(R.string.descricao_default_task))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.taskInsert.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.task_save, Toast.LENGTH_SHORT).show()

            findNavController().popBackStack()
        }

        viewModel.taskUpdate.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                R.string.task_editando_tarefa_sucesso,
                Toast.LENGTH_SHORT
            ).show()
            binding.progresbar.isVisible = false
        }
    }

}