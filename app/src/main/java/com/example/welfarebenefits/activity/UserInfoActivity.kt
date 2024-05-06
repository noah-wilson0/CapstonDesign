package com.example.welfarebenefits.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.databinding.ActivityUserInfoBinding
import com.example.welfarebenefits.entity.User
import java.io.Serializable

class UserInfoActivity : AppCompatActivity() {
    private var mBinding:ActivityUserInfoBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent= Intent()
        intent=getIntent()
        var user: User = intent.intentSerializable("user", User::class.java)!!
        Log.e("UserInfoActivity",user.toString())

        binding.backArrowImgInfo.setOnClickListener {
            var  data=Intent(this,MainActivity::class.java)
            startActivity(data)
        }


    }
}
//intent.getSerializableExtra()함수가 android api33 level 부터 deprecated 되었기 때문에  Deserialization을 위한 확장 함수 선언
fun <T: Serializable> Intent.intentSerializable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializableExtra(key, clazz)
    } else {
        this.getSerializableExtra(key) as T?
    }
}