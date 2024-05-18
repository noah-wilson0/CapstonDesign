package com.example.welfarebenefits.activity

import ShowCustomDialog
import ShowCustomDialogListener
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.R
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.util.ActivityStarter
import com.example.welfarebenefits.util.CitysSelectedListener
import com.example.welfarebenefits.util.ShowAlertDialog
import com.example.welfarebenefits.util.SignUp
import com.example.welfarebenefits.util.UserInfoValidator


class SignUpActivity : AppCompatActivity(), CitysSelectedListener {
    private var mBinding: ActivitySignUpBinding?=null
    private val binding get() = mBinding!!
    private var gender:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fragmentmanager=supportFragmentManager
        binding.residenceSelectBTN.setOnClickListener {
            val dialog = ShowCustomDialog( object : ShowCustomDialogListener {
                override fun onPositiveButtonClicked(selectedCity: String) {

                    Log.e("ShowCustomDialogListener",selectedCity)
                    binding.residenceTV.text = selectedCity // 선택된 도시를 TextView에 표시하거나 원하는 작업 수행
                }
            })
            dialog.show(fragmentmanager,null)
        }

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
                gender.isEmpty() ->{
                    ShowAlertDialog(this, "회원가입", "성별을 선택해 주세요!", "확인").showAlertDialog()
                }
                binding.inputAvgIncomeSesstionET.text.toString()
                    .isBlank() && binding.inputAvgIncomeSesstionET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원가입", "연 평균 소득을 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.residenceTV.text.toString()
                    .isBlank() && binding.residenceTV.text.toString().isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원가입", "거주지역을 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.inputIDET.text.toString().isNotBlank()
                        && binding.inputPSET.text.toString().isNotBlank()
                        && binding.inputNameET.text.toString().isNotBlank()
                        && gender.isNotEmpty()
                        && binding.inputAvgIncomeSesstionET.text.toString().isNotBlank()
                        && binding.residenceTV.text.toString().isNotBlank() -> {
                            if (UserInfoValidator().containsOnlyKoreanAndAlphabet(binding.inputNameET.text.toString())) {
                                if(UserInfoValidator().containsOnlyNumeric(binding.inputAvgIncomeSesstionET.text.toString())) {
                                    SignUp(this, binding).signUp(gender)
                                }
                                else{
                                    ShowAlertDialog(this, "회원가입", "연평균 소득에 특수문자와 숫자는 입력할 수 없습니다!", "확인").showAlertDialog()
                                }

                            }
                            else{
                                ShowAlertDialog(this, "회원가입", "이름에 특수문자와 숫자는 입력할 수 없습니다!", "확인").showAlertDialog()
                            }
                        }
                }
        }
            binding.radiogroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> {
                    gender= "남성"

                }
                R.id.female -> {
                    gender = "여성"
                }
            }
        }
        binding.cancel.setOnClickListener {
            ActivityStarter.startNextActivity(this,LogInActivity::class.java)
            finish()
        }


    }

    override fun onCitySelected(town: String) {
        Log.e("citysSelected",town)
        binding.residenceTV.text = town
    }


}