package com.example.welfarebenefits.util.database

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class NewUser() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    fun writeDatabaseNewUser(user:User) {
        database = Firebase.database.reference
        database.child(user.id).setValue(user)
    }
    fun addNewUserAccount(user:User,context: Context){
        auth = Firebase.auth
        val email:String=context.resources.getString(R.string.makeEmailTemplate)
        auth.createUserWithEmailAndPassword(user.id+email, user.password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    Log.d("signup", "signInWithEmail:success")

                } else {
                    Log.e("signup", "signInWithEmail:failure")

                }
            }

    }
}






