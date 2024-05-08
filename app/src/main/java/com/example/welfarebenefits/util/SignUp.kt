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

/*
 //아이디 중복인경우 : return false ->(db 추가 안됨)
 //비밀번호 중복인 경우: 검토 안함 -> return이 뭔지 확인불가 ->  db와 auth에 추가됨->"이미 사용중인 아이디입니다." 다이얼로그
  //이미 로그인한 유저인 경우 -> 처리해야됨

 */


class SignUp (val signUpActivity: SignUpActivity, private val binding: ActivitySignUpBinding){
    private lateinit var auth: FirebaseAuth
//    private  var success:Boolean=false

    fun signUp(){
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

//            NewUser().addNewUserAccount(user, signUpActivity)
//            NewUser().writeDatabaseNewUser(user)
            NewUser().addNewUserAccount(user, signUpActivity, object : NewUser.OnUserAccountAddedListener {
                override fun onUserAccountAdded() {
                    // 회원가입이 성공한 경우에만 데이터베이스에 사용자 데이터를 추가
                    NewUser().writeDatabaseNewUser(user)
//                    success = true
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
//                    success = false
                    ShowAlertDialog(
                        signUpActivity,
                        "회원가입",
                        "이미 사용중인 아이디입니다.",
                        "확인"
                    ).showAlertDialog()
                }
            })

        }
//        return success

    }


}


