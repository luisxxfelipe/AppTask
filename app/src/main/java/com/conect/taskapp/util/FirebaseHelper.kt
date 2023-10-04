package com.conect.taskapp.util

import com.conect.taskapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseHelper {
    companion object{
        fun getDataBase() = Firebase.database.reference

        fun getAuth() = FirebaseAuth.getInstance()

        fun getIdUser() = getAuth().currentUser?.uid ?: ""

        fun isAutenticated() = getAuth().currentUser != null

        fun validError(error: String): Int {
            return when {
                error.contains("There is no user record corresponding to this identifier") -> {
                    R.string.account_not_registered
                }
                error.contains("The email address is badly formatted") -> {
                    R.string.invalid_email_register
                }
                error.contains("The password is invalid or the user does not have a password") -> {
                    R.string.invalid_password_register
                }
                error.contains("The email address is already in use by another account") -> {
                    R.string.email_inUsed
                }
                error.contains("Password should be at least 6 characters") -> {
                    R.string.strong_password
                }

                else -> {
                    R.string.erro_generic
                }
            }
        }
    }
}