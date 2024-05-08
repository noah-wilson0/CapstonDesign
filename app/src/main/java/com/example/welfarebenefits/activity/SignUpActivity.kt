package com.example.welfarebenefits.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.util.ShowAlertDialog
import com.example.welfarebenefits.util.SignUp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
/*
    firebase는 email만 검증가능
 */
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
            when {
                binding.inputIDET.text.toString().isBlank() && binding.inputIDET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원가입", "아이디를 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.inputPSET.text.toString().isBlank() && binding.inputPSET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원가입", "비밀번호를 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.inputNameET.text.toString().isBlank() && binding.inputNameET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원가입", "이름을 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.inputAvgIncomeSesstionET.text.toString()
                    .isBlank() && binding.inputAvgIncomeSesstionET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원가입", "연 평균 소득을 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.familyStructureSesstionET.text.toString()
                    .isBlank() && binding.familyStructureSesstionET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원가입", "가족관계를 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.residenceSesstionET.text.toString()
                    .isBlank() && binding.residenceSesstionET.text.toString().isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원가입", "거주지역을 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.inputIDET.text.toString().isNotBlank()
                        && binding.inputPSET.text.toString().isNotBlank()
                        && binding.inputNameET.text.toString().isNotBlank()
                        && binding.inputAvgIncomeSesstionET.text.toString().isNotBlank()
                        && binding.familyStructureSesstionET.text.toString().isNotBlank()
                        && binding.residenceSesstionET.text.toString().isNotBlank() -> {
                            SignUp(this, binding).signUp()
                        }
                }
        }
        binding.cancel.setOnClickListener {
            startActivity(intent)
            finish()
        }


    }



}