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

    override fun onBookmarkImageClicked(id: String) {

    }

    override fun onAlertImageClicked() {

    }



    override fun onUserInfoImageClicked(id:String) {
        if(id=="guest"){
            onUserInfoClickListener?.onUserInfoClick(id)
            Log.e("onUserInfoImageClicked", "게스트 이벤트")
        }
        else {
            Log.e("onUserInfoImageClicked", "회원 정보 보기 이벤트 시작")
            database = Firebase.database.reference
            var user: User
            database.child(id).child("UserInfo").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val dataSnapshot = task.result
                    if (dataSnapshot.exists()) {
                        val userData = dataSnapshot.value as HashMap<String, *>

                        val id = userData["id"] as String ?: ""
                        val password = userData["password"] as String ?: ""
                        val name = userData["name"] as String ?: ""
                        val gender = userData["gender"] as String ?: ""
                        val residence = (userData["residence"] as List<String>)
                        val significantData = userData["significant"]  //[1인가구, 국가보훈대상] =List
                        val significant = mutableListOf<String?>()  // 혹은 원하는 타입으로 선언
                        if (significantData is List<*>) {
                            significant.addAll(significantData as List<String>)
                        }
                        user = User(
                            id=id,
                            password=password,
                            name=name,
                            gender=gender,
                            residence=residence,
                            significant=significant
                        )
                        Log.e("onUserInfoImageClicked", "User객체 생성완료")
                        onUserInfoClickListener?.onUserInfoClick(user)

                    }
                } else {
                    Firebase.auth.signOut()
                    Log.e("TAG", "db에서 데이터 가져오기 실패", task.exception)

                }
            }
        }
    }

    fun setOnUserInfoClickListener(listener: OnUserInfoClickListener) {
        this.onUserInfoClickListener = listener
    }

    override fun onUserInfoClick(user: User) {

    }
    override fun onUserInfoClick(id: String) {

    }


}



