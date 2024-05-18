package com.example.welfarebenefits.util

import android.app.Activity
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.activity.MainActivity
import com.example.welfarebenefits.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogIn {
    private lateinit var auth: FirebaseAuth

    fun logIn(activity: Activity, binding: ActivityLogInBinding) {
        auth = Firebase.auth
        val emailTemplate: String = activity.resources.getString(R.string.makeEmailTemplate)
        val fullEmail = binding.idET.text.toString().trim() + emailTemplate
        val password = binding.passwdET.text.toString().trim()

        Log.e("LOGIN", fullEmail)

        auth.signInWithEmailAndPassword(fullEmail, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.e("LOGIN", "signInWithEmail:success")
                    ActivityStarter.startNextActivity(activity, MainActivity::class.java, binding.idET.text.toString().trim())
                } else {
                    Log.e("LOGIN", "signInWithEmail:failure", task.exception)
                    handleLoginFailure(activity, binding)
                }
            }
    }

    private fun handleLoginFailure(activity: Activity, binding: ActivityLogInBinding) {
        val validator = FirebaseEmailValidator()
        validator.checkEmailExistence(binding.idET.text.toString().trim(), object : FirebaseEmailValidator.EmailCheckListener {
            override fun onEmailChecked(exists: Boolean) {
                val message = if (exists) {
                    // 이메일이 존재하는 경우의 처리
                    "비밀번호가 올바르지 않습니다."
                } else {
                    // 이메일이 존재하지 않는 경우의 처리
                    "존재하지 않는 이메일 주소입니다."
                }
                ShowAlertDialog(activity,
                    "로그인",
                    message,
                    "확인",
                    listener = object : ShowAlertDialogListener {
                        override fun onPositiveButtonClicked() {}
                        override fun onNegativeButtonClicked() {}
                    }).showAlertDialog()
            }
        })
    }
}
