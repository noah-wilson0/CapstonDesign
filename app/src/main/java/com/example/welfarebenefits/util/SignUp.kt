package com.example.welfarebenefits.util


import com.example.welfarebenefits.activity.SignUpActivity
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/*
    realtime DB에 데이터 추가하기
 */

class SignUp (val signUpActivity: SignUpActivity, private val binding: ActivitySignUpBinding){
    private lateinit var auth: FirebaseAuth
    private var success:Boolean=false
    fun signUp():Boolean{
        auth = Firebase.auth
        val user:User=User(
            binding.inputIDET.text.toString(),
            binding.inputPSET.text.toString(),
            binding.inputNameET.text.toString(),
            binding.inputAvgIncomeSesstionET.text.toString(),
            binding.familyStructureSesstionET.text.toString(),
            binding.residenceSesstionET.text.toString(),
            CheckedTextExtractor(
                listOf(
                    binding.singleHouseholdCHK,
                    binding.disabledPersonCHK,
                    binding.northKoreaDefectorCHK,
                    binding.multiFamilyCHK,
                    binding.singleFamilyCHK,
                    binding.militaryCHK),
                    binding).getCheckedTexts())
        val currentUser = auth.currentUser
        if (currentUser != null) {
            success=true
            /*
                realtime DB에 데이터 추가하기 util.databse에 클래스 추가하기
             */


        }
        return success
    }


}