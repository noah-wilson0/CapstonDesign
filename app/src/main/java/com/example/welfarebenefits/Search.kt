package com.example.welfarebenefits

import android.R
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.databinding.ActivitySearchBinding


class Search : AppCompatActivity() {
    private var mBinding:ActivitySearchBinding?=null
    private  val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchView.isSubmitButtonEnabled=true
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼을 눌렀을 때 호출되는 콜백
                // 여기에서 입력된 query를 이용하여 검색 결과를 처리할 수 있습니다.
                // 처리 후 true를 반환하여 이벤트 처리가 완료되었음을 알려줍니다.
                Toast.makeText(this@Search,query,Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어가 변경될 때 호출되는 콜백
                // 여기에서는 검색어의 변경에 따라 필요한 동작을 수행할 수 있습니다.
                // 처리 후 true 또는 false를 반환하여 이벤트 처리가 완료되었음을 알려줍니다.
                return true
            }
        })


    }
    override fun onBackPressed() {
        if (!binding.searchView.isIconified) {
            binding.searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }
}

