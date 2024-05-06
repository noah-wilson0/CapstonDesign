package com.example.welfarebenefits.util


import android.util.Log
import com.example.welfarebenefits.activity.SignUpActivity
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.util.database.NewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUp (val signUpActivity: SignUpActivity, private val binding: ActivitySignUpBinding){
    private lateinit var auth: FirebaseAuth
    private  var success=false

    fun signUp():Boolean{
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.e("signUp","이미 로그인한 유저:회원가입 불가능")
        }
        else {
            success=true
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
        }
        return success

    }


}


