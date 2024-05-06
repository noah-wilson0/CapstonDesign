package com.example.welfarebenefits.util

import android.util.Log
import com.example.welfarebenefits.entity.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class ToolbarMenuItemClickListener():ToolbarMenuItemClickListeners,OnUserInfoClickListener {

    private lateinit var database: DatabaseReference
    private var onUserInfoClickListener: OnUserInfoClickListener? = null

    override fun onSearchImageClicked() {

    }

    override fun onAlertImageClicked() {

    }



    override fun onUserInfoImageClicked(id:String) {
        database = Firebase.database.reference
        var user:User
        database.child(id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.value as HashMap<String, *>

                    val id = userData["id"] as String?:""
                    val password = userData["password"]as String?:""
                    val name = userData["name"]as String?:""
                    val avgIncome = userData["avgIncome"]as String?:""
                    val familyStructure = userData["familyStructure"]as String?:""
                    val residence = userData["residence"]as String?:""
                    val significantData= userData["significant"]  //[1인가구, 국가보훈대상] =List
                    val significant = mutableListOf<String?>()  // 혹은 원하는 타입으로 선언
                    if (significantData is List<*>) {
                        significant.addAll(significantData as List<String>)
                    }
                    user = User(id, password, name, avgIncome, familyStructure, residence, significant)
                    onUserInfoClickListener?.onUserInfoClick(user)

                }
            } else {
                Firebase.auth.signOut()
                Log.w("TAG", "db에서 데이터 가져오기 실패", task.exception)

            }
        }
    }

    fun setOnUserInfoClickListener(listener: OnUserInfoClickListener) {
        this.onUserInfoClickListener = listener
    }

    override fun onUserInfoClick(user: User) {

    }


}



