package com.example.welfarebenefits.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.databinding.ActivityLogInBinding
import com.example.welfarebenefits.util.ActivityStarter
import com.example.welfarebenefits.util.LogIn
import com.example.welfarebenefits.util.ShowAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
            Log.e("LogInActivity","회원가입하기 클릭")
            val currentUser = auth.currentUser
            if (currentUser != null) {
                Log.e("LogInActivity","이미 회원가입한 사용자")
                ShowAlertDialog(this,"로그인",
                    "이미 가입된 사용자입니다. 이미 가입한 계정으로 로그인해주세요",
                    "확인").showAlertDialog()
            }
            else {
                Log.e("LogInActivity","SignUpActivity로 이동")
                ActivityStarter.startNextActivityNotFinish(this,SignUpActivity::class.java)
            }
        }
        binding.gestLogIn.setOnClickListener{ guestLogin() }

    }

    //게스트 사용자는 어느 기능까지 해줄건지 고민하기 허용되는 권한에 따라 db를 추가해야할듯
    private fun guestLogin() {
        val user=auth.currentUser
        if (user != null) {
            ActivityStarter.startNextActivity(this,MainActivity::class.java)
        } else {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        ActivityStarter.startNextActivity(this, MainActivity::class.java)
                    } else {
                        Log.e("GUESTLOGIN","익명 로그인 실패")
                    }
                }

        }

    }




}