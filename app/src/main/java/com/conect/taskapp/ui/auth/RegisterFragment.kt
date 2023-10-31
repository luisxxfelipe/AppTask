package com.conect.taskapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.conect.taskapp.R
import com.conect.taskapp.databinding.FragmentRegisterBinding
import com.conect.taskapp.ui.BaseFragment
import com.conect.taskapp.util.FirebaseHelper
import com.conect.taskapp.util.initToolBar
import com.conect.taskapp.util.showBottomSheet

class RegisterFragment : BaseFragment() {

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

    private fun initListener() {
        binding.btncriarConta.setOnClickListener {
            validateData()
            //findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()
        val passaword = binding.editSenha.text.toString().trim()

        if (email.isNotEmpty()) {
            if (passaword.isNotEmpty()) {

                hideKeyboard()
                binding.progresbar.isVisible = true
                registerUser(email, passaword)
            } else {
                showBottomSheet(message = getString(R.string.senha_default_register))
            }
        } else {
            showBottomSheet(message = getString(R.string.email_default_register))
        }
    }

    private fun registerUser(email: String, password: String) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    binding.progresbar.isVisible = false
                    showBottomSheet(
                        message = getString(FirebaseHelper.validError(task.exception?.message.toString()))
                    )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}