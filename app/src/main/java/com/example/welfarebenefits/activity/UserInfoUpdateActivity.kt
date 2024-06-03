package com.example.welfarebenefits.activity

import ShowCustomDialog
import ShowCustomDialogListener
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.util.ActivityStarter
import com.example.welfarebenefits.util.JsonConverter
import com.example.welfarebenefits.util.ShowAlertDialog
import com.example.welfarebenefits.util.SignUp
import com.example.welfarebenefits.util.UserInfoUpdater
import com.example.welfarebenefits.util.UserInfoValidator
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class UserInfoUpdateActivity : AppCompatActivity() {
    private var mBinding: ActivitySignUpBinding?=null
    private val binding get() = mBinding!!
    private var user: User? = null
    private var gender:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent= Intent()
        intent = getIntent()
        val userJson = intent.getStringExtra("user")
        user = JsonConverter().jsonToUser(userJson!!)
        Log.e("UserInfoActivity", user.toString())

        binding.inputIDET.setText(user!!.id)
        binding.inputIDET.isEnabled = false
        binding.inputPSET.setText(user!!.password)
        binding.inputNameET.setText(user!!.name)
        binding.residenceTV.setText(user!!.residence)
        if(user!!.gender.equals("남성")) binding.male.isChecked = true
        else binding.female.isChecked = true
        if(user!!.significant.contains(binding.singleHouseholdCHK.text)) binding.singleHouseholdCHK.isChecked = true
        if(user!!.significant.contains(binding.singleFamilyCHK.text)) binding.singleFamilyCHK.isChecked = true
        if(user!!.significant.contains(binding.multiChildFamilyCHK.text)) binding.multiChildFamilyCHK.isChecked = true
        if(user!!.significant.contains(binding.multiFamilyCHK.text)) binding.multiFamilyCHK.isChecked = true
        if(user!!.significant.contains(binding.northKoreaDefectorCHK.text)) binding.northKoreaDefectorCHK.isChecked = true
        if(user!!.significant.contains(binding.jobseeker.text)) binding.jobseeker.isChecked = true
        if(user!!.significant.contains(binding.jobless.text)) binding.jobless.isChecked = true
        if(user!!.significant.contains(binding.militaryCHK.text)) binding.militaryCHK.isChecked = true
        binding.cancel.text = "회원탈퇴"
        binding.cancel.setTextColor(Color.RED)
        binding.submit.text = "회원정보수정"
        binding.submit.setTextColor(Color.BLACK)

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

        binding.backArrowImgInfo.setOnClickListener{
            ActivityStarter.startNextActivity(this,UserInfoActivity::class.java,userJson)
        }

        binding.submit.setOnClickListener {
            when {
                binding.inputIDET.text.toString().isBlank() && binding.inputIDET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원정보수정", "아이디를 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.inputPSET.text.toString().isBlank() && binding.inputPSET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원정보수정", "비밀번호를 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.inputNameET.text.toString().isBlank() && binding.inputNameET.text.toString()
                    .isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원정보수정", "이름을 입력해 주세요!", "확인").showAlertDialog()
                }
                gender.isEmpty() ->{
                    ShowAlertDialog(this, "회원정보수정", "성별을 선택해 주세요!", "확인").showAlertDialog()
                }
                binding.residenceTV.text.toString()
                    .isBlank() && binding.residenceTV.text.toString().isNullOrEmpty() -> {
                    ShowAlertDialog(this, "회원정보수정", "거주지역을 입력해 주세요!", "확인").showAlertDialog()
                }

                binding.inputIDET.text.toString().isNotBlank()
                        && binding.inputPSET.text.toString().isNotBlank()
                        && binding.inputNameET.text.toString().isNotBlank()
                        && gender.isNotEmpty()
                        && binding.residenceTV.text.toString().isNotBlank() -> {
                    if (UserInfoValidator().containsOnlyKoreanAndAlphabet(binding.inputNameET.text.toString().trim())) {
                        val newUserInfo = UserInfoUpdater(binding).updateUserInfo(gender, this)
                        ActivityStarter.startNextActivity(this, UserInfoActivity::class.java, newUserInfo)
                    }
                    else{
                        ShowAlertDialog(this, "회원정보수정", "이름에 특수문자와 숫자는 입력할 수 없습니다!", "확인").showAlertDialog()
                    }
                }
            }
        }
        binding.cancel.setOnClickListener{
            UserInfoUpdater(binding).deleteUserAccount(binding.inputIDET.text.toString())
            Firebase.auth.signOut()
            val sharedPreferences: SharedPreferences =getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val editor=sharedPreferences.edit()
            editor.clear()
            editor.commit()
            ActivityStarter.startNextActivity(this,LogInActivity::class.java)
        }
    }
}