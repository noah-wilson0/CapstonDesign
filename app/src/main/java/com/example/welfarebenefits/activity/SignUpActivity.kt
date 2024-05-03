package com.example.welfarebenefits.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.util.ShowAlertDialog
import com.example.welfarebenefits.util.ShowAlertDialogListener
import com.example.welfarebenefits.util.SignUp

class SignUpActivity : AppCompatActivity() {
    private var mBinding: ActivitySignUpBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent=Intent(this,LogInActivity::class.java)

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