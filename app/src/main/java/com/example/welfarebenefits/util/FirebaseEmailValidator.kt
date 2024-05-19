package com.example.welfarebenefits.util

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class FirebaseEmailValidator {
    private lateinit var database: DatabaseReference

    interface EmailCheckListener {
        fun onEmailChecked(exists: Boolean)
    }

    fun checkEmailExistence(id:String, listener: EmailCheckListener) {
        database = Firebase.database.reference.child(id)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터가 존재하면 true를 반환
                val exists = dataSnapshot.exists()
                listener.onEmailChecked(exists)
            }

            override fun onCancelled(error: DatabaseError) {
                // 작업이 취소되거나 실패하면 false를 반환
                listener.onEmailChecked(false)
            }
        })
    }
}
