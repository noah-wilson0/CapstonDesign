package com.example.welfarebenefits.util


import android.util.Log
import com.example.welfarebenefits.activity.LogInActivity
import com.example.welfarebenefits.activity.SignUpActivity
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.util.database.NewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase




class SignUp (val signUpActivity: SignUpActivity, private val binding: ActivitySignUpBinding){
    private lateinit var auth: FirebaseAuth

    fun signUp(gender:String){
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.e("signUp","이미 로그인한 유저:회원가입 불가능")
            ShowAlertDialog(
                signUpActivity,
                "회원가입",
                "이미 가입된 사용자입니다. 이미 가입한 계정으로 로그인해주세요",
                "확인"
            ).showAlertDialog()
        }
        else {
            val name:String=UserInfoValidator().validateName(binding.inputNameET.text.toString().trim())


            val user: User = User(
                id = binding.inputIDET.text.toString().trim(),
                password = binding.inputPSET.text.toString().trim(),
                name = name,
                gender = gender,
                residence = binding.residenceTV.text.toString().trim(),
                significant =  CheckedTextExtractor(
                    mutableListOf(
                        binding.singleHouseholdCHK,
                        binding.singleFamilyCHK,
                        binding.multiChildFamilyCHK,
                        binding.childbirth,
                        binding.multiFamilyCHK,
                        binding.disabledPersonCHK,
                        binding.northKoreaDefectorCHK,
                        binding.jobseeker,
                        binding.jobless,
                        binding.militaryCHK
                    ),
                    binding
                ).getCheckedTexts()
            )
            Log.e("SignUp", "회원가입 진행")

            NewUser().addNewUserAccount(user, signUpActivity, object : NewUser.OnUserAccountAddedListener {
                override fun onUserAccountAdded() {
                    // 회원가입이 성공한 경우에만 데이터베이스에 사용자 데이터를 추가
                    NewUser().writeDatabaseNewUser(user)
                    ShowAlertDialog(signUpActivity,
                        "회원가입",
                        "회원가입이 완료되었습니다!",
                        "확인",
                        listener = object : ShowAlertDialogListener {
                            override fun onPositiveButtonClicked() {
                               ActivityStarter.startNextActivity(signUpActivity,LogInActivity::class.java)
                            }

                            override fun onNegativeButtonClicked() {

                            }
                        }).showAlertDialog()
                }
                override fun onUserAccountAddFailed() {
                    // 회원가입 실패한 경우
                    ShowAlertDialog(
                        signUpActivity,
                        "회원가입",
                        "이미 사용중인 아이디입니다.",
                        "확인"
                    ).showAlertDialog()
                }
            })

        }
    }


}


