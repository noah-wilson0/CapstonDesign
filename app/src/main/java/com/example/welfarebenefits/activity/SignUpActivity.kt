package com.example.welfarebenefits.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.util.ShowAlertDialog
import com.example.welfarebenefits.util.ShowAlertDialogListener
import com.example.welfarebenefits.util.SignUp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {
    private var mBinding: ActivitySignUpBinding?=null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent=Intent(this,LogInActivity::class.java)
        auth = Firebase.auth


        binding.submit.setOnClickListener {

            var success:Boolean=SignUp(this,binding).signUp()
            when (success) {
                true -> {
                    ShowAlertDialog(this,
                        "회원가입",
                        "회원가입이 완료되었습니다!",
                        "확인",
                        listener = object : ShowAlertDialogListener {
                            override fun onPositiveButtonClicked() {
                                startActivity(intent)
                                finish()
                            }

                            override fun onNegativeButtonClicked() {

                            }
                        }).showAlertDialog()
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        Log.e("SignUpActivity","회원가입후currentuser는 null이 아님")
                        Log.e("SignUpActivity",currentUser.toString())
                    }
                    else
                    {
                        Log.e("SignUpActivity","회원가입후currentuser는 null임")
                    }
                }
                false -> {
                    ShowAlertDialog(this,"회원가입","회원가입에 실패했습니다. 죄송합니다. 다시 시도해주세요.","확인").showAlertDialog()
                }
                else -> {
                    moveTaskToBack(true);						// 태스크를 백그라운드로 이동
                    finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
                    android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료
                }
            }
        }
        binding.cancel.setOnClickListener {
            startActivity(intent)
            finish()
        }


    }



}