package com.example.welfarebenefits.util.database

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class NewUser() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    interface OnUserAccountAddedListener {
        fun onUserAccountAdded()
        fun onUserAccountAddFailed()
    }
    fun addNewUserAccount(user:User,context: Context, listener: OnUserAccountAddedListener){

        auth = Firebase.auth
        val email:String=context.resources.getString(R.string.makeEmailTemplate)
        auth.createUserWithEmailAndPassword(user.id+email, user.password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    Log.e("signup" +
                            "", "회원가입 성공")
                    listener.onUserAccountAdded()
                } else {
                    Log.e("signup", "회원가입 실패")
                    if (task.exception is FirebaseAuthUserCollisionException){
                        Log.e("sinup", "id/ps중복")
                        listener.onUserAccountAddFailed()

                    }

                }
            }

    }
    fun writeDatabaseNewUser(user:User) {
        database = Firebase.database.reference
        database.child(user.id).setValue(user)
    }

}






