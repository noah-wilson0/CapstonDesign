package com.example.welfarebenefits.util


import android.util.Log
import com.example.welfarebenefits.activity.SignUpActivity
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.util.database.NewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
/*
                NewUser().writeDatabaseNewUser(user)
            NewUser().addNewUserAccount(user, signUpActivity)
            를 실행에 따라 NewUser로 부터 Exception에을 반환받고
            이를 다시 SihnUp Activity에 return해주면 될듯?(의존성 문제 생기려나)
            *일단 유저는 currentUser가 null이든 아니든 로그인이 진행되어야하는게 정상*
 */

class SignUp (val signUpActivity: SignUpActivity, private val binding: ActivitySignUpBinding){
    private lateinit var auth: FirebaseAuth


    fun signUp():Boolean{

        auth = Firebase.auth
        val user: User = User(
            binding.inputIDET.text.toString().trim(),
            binding.inputPSET.text.toString().trim(),
            binding.inputNameET.text.toString().trim(),
            binding.inputAvgIncomeSesstionET.text.toString().trim(),
            binding.familyStructureSesstionET.text.toString().trim(),
            binding.residenceSesstionET.text.toString().trim(),
            CheckedTextExtractor(
                mutableListOf(
                    binding.singleHouseholdCHK,
                    binding.disabledPersonCHK,
                    binding.northKoreaDefectorCHK,
                    binding.multiFamilyCHK,
                    binding.singleFamilyCHK,
                    binding.militaryCHK
                ),
                binding
            ).getCheckedTexts()
        )
        Log.e("SignUp", "회원가입 진행")
        NewUser().writeDatabaseNewUser(user)
        NewUser().addNewUserAccount(user, signUpActivity)
        return true

    }


}


