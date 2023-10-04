package com.conect.taskapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.conect.taskapp.R
import com.conect.taskapp.databinding.FragmentRecoverAccountBinding
import com.conect.taskapp.util.FirebaseHelper
import com.conect.taskapp.util.initToolBar
import com.conect.taskapp.util.showBottonSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecoverAccountFragment : Fragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

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

        auth = Firebase.auth

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
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->

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