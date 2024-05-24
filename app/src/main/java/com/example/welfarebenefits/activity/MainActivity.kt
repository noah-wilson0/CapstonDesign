package com.example.welfarebenefits.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfarebenefits.R
import com.example.welfarebenefits.adapter.RecyclerViewAdapter
import com.example.welfarebenefits.databinding.ActivityMainBinding

import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.entity.WelfareCategoryMap
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.ActivityStarter
import com.example.welfarebenefits.util.CallBackWelfareData
import com.example.welfarebenefits.util.OnUserInfoClickListener
import com.example.welfarebenefits.util.ToolbarMenuItemClickListener
import com.example.welfarebenefits.util.WelfareDataFetcher
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityMainBinding?=null
//    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private val binding get() = mBinding!!
    private var id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent=Intent()
        intent=getIntent()
        if(intent.getStringExtra("id").isNullOrEmpty()){
            id="guest"
        }

        else{
            id= intent.getStringExtra("id")!!
        }
        Log.e("MAIN",id)
        //임시 로그아웃 구현
        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            intent= Intent(this,LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.searchImage -> {
                        // 검색 이미지를 클릭한 경우의 동작
                        Toast.makeText(this@MainActivity, "검색 이미지를 클릭했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        return true
                    }

                    R.id.alertImage -> {

                        // 알림 이미지를 클릭한 경우의 동작
                        Toast.makeText(this@MainActivity, "알림 이미지를 클릭했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        return true
                    }

                    R.id.userInfoImage -> {
                        val listener = ToolbarMenuItemClickListener()
                        listener.setOnUserInfoClickListener(object : OnUserInfoClickListener {
                            override fun onUserInfoClick(user: User) {
                                ActivityStarter.startNextActivity(this@MainActivity,UserInfoActivity::class.java,user)
                            }

                            override fun onUserInfoClick(id: String) {
                                ActivityStarter.startNextActivity(this@MainActivity,UserInfoActivity::class.java,id)
                            }
                        })
                        listener.onUserInfoImageClicked(id)
                        return true
                    }

                    else -> false
                }
            }
        })
        val sortingCriteriaArray = resources.getStringArray(R.array.sorting_criteria)
        var spinnerAdapter =
            ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, sortingCriteriaArray)
        binding.mainListSelectSpinner.adapter = spinnerAdapter

        binding.mainListSelectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sortingCriteriaArray[position]
                Log.e("MainActivitymainListSelectSpinner",selectedItem)
                val recyclerViewAdapter = WelfareCategoryMap.getCategoryMap(selectedItem)
                    ?.let { RecyclerViewAdapter(it) }
                Log.e("MainActivitymainListSelectSpinner", recyclerViewAdapter?.itemCount.toString())
                recyclerViewAdapter?.notifyDataSetChanged()
                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.recyclerView.adapter = recyclerViewAdapter



            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 이벤트
            }
        }

        binding.mainSort.setOnClickListener(this)
        binding.kAlphabetSort.setOnClickListener(this)
        binding.viewsSort.setOnClickListener(this)

        WelfareDataFetcher().getWelfareData(object : CallBackWelfareData {
            override fun getWelfareData(welfareDataList: List<WelfareData>) {
                Log.e("MainActivity", "Received welfare data: ${welfareDataList.size} items")
                val recyclerViewAdapter = RecyclerViewAdapter(welfareDataList)
                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.recyclerView.adapter = recyclerViewAdapter
            }
        })


    }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.main_sort -> {
                    Toast.makeText(this, "Main Sort 버튼 클릭", Toast.LENGTH_SHORT).show()
                    binding.mainSort.setTypeface(null, Typeface.BOLD)
                    binding.kAlphabetSort.setTypeface(null, Typeface.NORMAL)
                    binding.viewsSort.setTypeface(null, Typeface.NORMAL)
                }
                R.id.k_alphabet_sort -> {
                    Toast.makeText(this, "가나다순 정렬 버튼 클릭", Toast.LENGTH_SHORT).show()
                    binding.mainSort.setTypeface(null, Typeface.NORMAL)
                    binding.kAlphabetSort.setTypeface(null, Typeface.BOLD)
                    binding.viewsSort.setTypeface(null, Typeface.NORMAL)
                }
                R.id.views_sort -> {
                    Toast.makeText(this, "조회수 정렬 버튼 클릭", Toast.LENGTH_SHORT).show()
                    binding.mainSort.setTypeface(null, Typeface.NORMAL)
                    binding.kAlphabetSort.setTypeface(null, Typeface.NORMAL)
                    binding.viewsSort.setTypeface(null, Typeface.BOLD)
                }
                else -> {
                    // 다른 뷰 클릭 시 처리
                }
            }


    }



}


