package com.conect.taskapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.conect.taskapp.R
import com.conect.taskapp.databinding.FragmentLoginBinding
import com.conect.taskapp.util.showBottonSheet

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener(){
        binding.btnLogin.setOnClickListener{
            validateData()
            //findNavController().navigate(R.id.action_global_homeFragment)
        }

        binding.btnRegistener.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnRecover.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    private fun validateData(){
        val email = binding.editEmail.text.toString().trim()
        val passaword = binding.editSenha.text.toString().trim()

        if(email.isNotEmpty()){
            if(passaword.isNotEmpty()){
                findNavController().navigate(R.id.action_global_homeFragment)

            }else{
                showBottonSheet(message = getString(R.string.senha_default))
            }
        }else{
            showBottonSheet(message = getString(R.string.email_default))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}