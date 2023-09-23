package com.conect.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.conect.taskapp.R
import com.conect.taskapp.databinding.FragmentFormTaskBinding
import com.conect.taskapp.databinding.FragmentRegisterBinding
import com.conect.taskapp.util.initToolBar
import com.conect.taskapp.util.showBottonSheet


class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

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
        initListener()
    }

    private fun initListener(){
        binding.btnSave.setOnClickListener{
            validateData()
            //findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    private fun validateData(){
        val description = binding.editDescription.text.toString().trim()

        if(description.isNotEmpty()){
            Toast.makeText(requireContext(), "Concluido com sucesso!!", Toast.LENGTH_SHORT).show()
        }else{
            showBottonSheet(message = getString(R.string.descricao_default_task))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}