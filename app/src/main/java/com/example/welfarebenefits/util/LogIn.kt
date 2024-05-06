package com.example.welfarebenefits.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.activity.MainActivity
import com.example.welfarebenefits.databinding.ActivityLogInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LogIn() {
    private lateinit var auth: FirebaseAuth
    fun logIn(activity: Activity,binding: ActivityLogInBinding,){
        auth = Firebase.auth
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            Log.e("LOGIN","이미 로그인한 유저")
//            Log.e("LOGIN",currentUser.toString())
//        }
//        else{
            val email:String=activity.resources.getString(R.string.makeEmailTemplate)
            Log.e("LOGIN",binding.idET.text.toString()+email)
            auth.signInWithEmailAndPassword(binding.idET.text.toString().trim()+email, binding.passwdET.text.toString().trim())
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.e("LOGIN", "signInWithEmail:success")
                        val intent = Intent(activity, MainActivity::class.java)
                        activity.startActivity(intent)

                    } else {
                        Log.e("LOGIN", "signInWithEmail:failure", task.exception)
                        ShowAlertDialog(activity,
                            "로그인",
                            "아이디 또는 비밀번호가 일치하지 않습니다.",
                            "확인",
                            listener = object :
                                ShowAlertDialogListener {
                                override fun onPositiveButtonClicked() {
                                }

                                override fun onNegativeButtonClicked() {
                                }
                            }).showAlertDialog()
                    }
                }
//        }
    }
}