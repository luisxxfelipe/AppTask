package com.conect.taskapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.conect.taskapp.R
import com.conect.taskapp.databinding.FragmentRecoverAccountBinding
import com.conect.taskapp.ui.BaseFragment
import com.conect.taskapp.util.FirebaseHelper
import com.conect.taskapp.util.initToolBar
import com.conect.taskapp.util.showBottonSheet


class RecoverAccountFragment : BaseFragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar)

        initListener()
    }

    private fun initListener() {
        binding.btnrecuperarConta.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()

        if (email.isNotEmpty()) {

            hideKeyboard()
            binding.progresbar.isVisible = true

            recoverAccountUser(email)
            Toast.makeText(
                requireContext(),
                "Link para redefinição de senha enviado com sucesso.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            showBottonSheet(message = getString(R.string.email_default))
        }
    }

    private fun recoverAccountUser(email: String) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener { task ->

            binding.progresbar.isVisible = false

            if (task.isSuccessful) {
                showBottonSheet(message = getString(R.string.text_dialogo_recover))

            } else {
                showBottonSheet(
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