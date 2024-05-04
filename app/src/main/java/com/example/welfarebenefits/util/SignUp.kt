package com.example.welfarebenefits.util


import com.example.welfarebenefits.activity.SignUpActivity
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.util.database.NewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class SignUp (val signUpActivity: SignUpActivity, private val binding: ActivitySignUpBinding){
    private lateinit var auth: FirebaseAuth
    private var success:Boolean=false

    fun signUp():Boolean{
        auth = Firebase.auth
        val user:User=User(
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
                    binding.militaryCHK),
                    binding).getCheckedTexts())
        val currentUser = auth.currentUser
        if (currentUser != null) {
            success=true
            NewUser().writeDatabaseNewUser(user)
            NewUser().addNewUserAccount(user,signUpActivity)
        }else{ }
        return success
    }


}


