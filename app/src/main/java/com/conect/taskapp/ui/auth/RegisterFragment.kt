package com.conect.taskapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.conect.taskapp.R
import com.conect.taskapp.databinding.FragmentRegisterBinding
import com.conect.taskapp.util.initToolBar
import com.conect.taskapp.util.showBottonSheet
import java.util.zip.Inflater

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar)

        initListener()
    }

    private fun initListener(){
        binding.btncriarConta.setOnClickListener{
            validateData()
            //findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    private fun validateData(){
        val email = binding.editEmail.text.toString().trim()
        val passaword = binding.editSenha.text.toString().trim()

        if(email.isNotEmpty()){
            if(passaword.isNotEmpty()){
                Toast.makeText(requireContext(), "Conta criada com sucesso.", Toast.LENGTH_SHORT).show()
            }else{
                showBottonSheet(message = getString(R.string.senha_default_register))
            }
        }else{
            showBottonSheet(message = getString(R.string.email_default_register))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}