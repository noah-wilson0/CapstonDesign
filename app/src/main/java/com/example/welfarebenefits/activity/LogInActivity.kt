package com.example.welfarebenefits.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.welfarebenefits.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Intent
import com.example.welfarebenefits.util.LogIn
import com.example.welfarebenefits.util.ShowAlertDialog
import com.example.welfarebenefits.util.ShowAlertDialogListener

class LogInActivity : AppCompatActivity() {
    private var mBinding:ActivityLogInBinding?=null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.loginbutton.setOnClickListener {
            when {
                binding.idET.text.toString().isBlank()&&binding.idET.text.toString().isNullOrEmpty() -> {
                    ShowAlertDialog(this,"로그인","아이디를 입력해 주세요!","확인").showAlertDialog()
                }
                binding.passwdET.text.toString().isBlank()&&binding.passwdET.text.toString().isNullOrEmpty() -> {
                    ShowAlertDialog(this,"로그인","비밀번호를 입력해 주세요!","확인").showAlertDialog()
                }
                binding.idET.text.toString().isNotBlank()&&binding.passwdET.text.toString().isNotBlank() ->{
                    LogIn().logIn(this,binding)
                }
                else -> {
                    Log.e("LOGIN","로그인 오류 발생")
                    finish()
                }
            }
        }

        binding.singUpTV.setOnClickListener {
            Log.e("LOGIN","문제")
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.gestLogIn.setOnClickListener{ guestLogin() }

    }

    //게스트 사용자는 어느 기능까지 해줄건지 고민하기 허용되는 권한에 따라 db를 추가해야할듯
    private fun guestLogin() {
        val user=auth.currentUser
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
            finish()
        } else {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java);
                        startActivity(intent)
                        finish()

                    } else {
                        Log.e("GUESTLOGIN","익명 로그인 실패")
                    }
                }

        }

    }




}