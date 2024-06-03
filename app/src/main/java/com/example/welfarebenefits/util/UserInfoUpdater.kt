package com.example.welfarebenefits.util

import android.content.Context
import android.util.Log
import androidx.core.app.GrammaticalInflectionManagerCompat.GrammaticalGender
import com.example.welfarebenefits.R
import com.example.welfarebenefits.databinding.ActivitySignUpBinding
import com.example.welfarebenefits.databinding.ActivityUserInfoBinding
import com.example.welfarebenefits.entity.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class UserInfoUpdater(private val binding:ActivitySignUpBinding) {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    fun updateUserInfo(gender: String, context: Context): User{
        val newPassword = binding.inputPSET.text.toString()
        updatePassword(newPassword, object : UpdatePasswordCallback {
            override fun onSuccess() {
                // 비밀번호 업데이트 성공 시 처리할 로직
                Log.e("updatePW", "콜백: 비밀번호 업데이트 완료")
            }

            override fun onFailure(exception: Exception) {
                // 비밀번호 업데이트 실패 시 처리할 로직
                Log.e("updatePW", "콜백: 비밀번호 업데이트 실패 - ${exception.message}")
            }
        })
        val name:String=UserInfoValidator().validateName(binding.inputNameET.text.toString().trim())
        val user = User(
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
        database = Firebase.database.reference
        database.child(user.id).child(context.getString(R.string.UserInfo)).setValue(user)
        return user
    }

    fun updatePassword(newPassword: String, callback: UpdatePasswordCallback) {
        auth = Firebase.auth
        val currentUser = auth.currentUser!!
        currentUser.updatePassword(newPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("updatePW", "비밀번호 업데이트 완료")
                callback.onSuccess()
            } else {
                Log.e("updatePW", "비밀번호 업데이트 실패")
                task.exception?.let {
                    callback.onFailure(it)
                }
            }
        }
    }

    fun deleteUserAccount(id:String){
        deleteUser(id, object : DeleteUserCallback {
            override fun onSuccess() {
                // 유저 삭제 성공 시 처리할 로직
                Log.e("deleteUser", "콜백: 유저 삭제 성공")
            }

            override fun onFailure(exception: Exception) {
                // 유저 삭제 실패 시 처리할 로직
                Log.e("deleteUser", "콜백: 유저 삭제 실패 - ${exception.message}")
            }
        })
    }

    fun deleteUser(id: String, callback: DeleteUserCallback) {
        auth = Firebase.auth
        val user = auth.currentUser!!
        user.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("deleteUser", "유저 계정 삭제 성공")
                database = Firebase.database.reference
                database.child(id).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e("deleteUserInfo", "유저 정보 삭제 성공")
                        callback.onSuccess()
                    } else {
                        Log.e("deleteUserInfo", "유저 정보 삭제 실패")
                        task.exception?.let { callback.onFailure(it) }
                    }
                }
            } else {
                Log.e("deleteUser", "유저 계정 삭제 실패")
                task.exception?.let { callback.onFailure(it) }
            }
        }
    }


}
interface UpdatePasswordCallback {
    fun onSuccess()
    fun onFailure(exception: Exception)
}
interface DeleteUserCallback {
    fun onSuccess()
    fun onFailure(exception: Exception)
}
