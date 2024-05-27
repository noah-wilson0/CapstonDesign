package com.example.welfarebenefits.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfarebenefits.R
import com.example.welfarebenefits.adapter.OnItemClickListener
import com.example.welfarebenefits.adapter.RecyclerViewAdapter
import com.example.welfarebenefits.databinding.ActivityMainBinding
import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.entity.WelfareCategoryMap
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.ActivityStarter
import com.example.welfarebenefits.util.AlarmTest
import com.example.welfarebenefits.util.CallBackWelfareData
import com.example.welfarebenefits.util.OnUserInfoClickListener
import com.example.welfarebenefits.util.ToolbarMenuItemClickListener
import com.example.welfarebenefits.util.WelfareDataFetcher
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity(), View.OnClickListener, OnItemClickListener {
    private var mBinding: ActivityMainBinding?=null
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1


    private val binding get() = mBinding!!
    private var id:String=""
    private lateinit var welfareDataList:List<WelfareData>

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
        // 알림 권한 요청
        requestNotificationPermission()
        binding.alerm.setOnClickListener {
            Log.e("MAIN","알림시작")
            AlarmTest(id,this).deliverNotification(welfareDataList[(welfareDataList.indices).random()])
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
                                ActivityStarter.startNextActivityNotFinish(this@MainActivity,UserInfoActivity::class.java,user)
                            }

                            override fun onUserInfoClick(id: String) {
                                ActivityStarter.startNextActivityNotFinish(this@MainActivity,UserInfoActivity::class.java,id)
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
                    ?.let { RecyclerViewAdapter(it, this@MainActivity) }
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
        WelfareDataFetcher().getWelfareData(this,id,object : CallBackWelfareData {
            override fun getWelfareData(welfareDataList: List<WelfareData>) {
                Log.e("MainActivity", "Received welfare data: ${welfareDataList.size} items")

                val recyclerViewAdapter = RecyclerViewAdapter(welfareDataList,this@MainActivity)
                this@MainActivity.welfareDataList=welfareDataList

                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.recyclerView.adapter = recyclerViewAdapter
            }
        })


    }
    override fun onItemClick(position: Int) {
        val clickedItem = binding.recyclerView.adapter?.let { adapter ->
            if (adapter is RecyclerViewAdapter) {
                adapter.getItem(position)
            } else null
        }
        clickedItem?.let {
            val intent = Intent(this, SubListActivity::class.java).apply {
                putExtra("SERVICE_URL", it.detailURL)
                putExtra("SERVICE_ID", it.serviceID)
                putExtra("SERVICE_NAME", it.serviceName)
                putExtra("SERVICE_SUMMARY", it.serviceSummary)
                putExtra("SERVICE_CRITERIA", it.selectionCriteria)
                putExtra("SERVICE_DEADLINE", it.applicationDeadline)
                putExtra("SERVICE_METHOD", it.applicationMethod)
                putExtra("SERVICE_CONTENT", it.supportContent)
            }
            startActivity(intent)
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 권한이 허용되었을 때 (필요시 추가 동작)
                Log.d("MainActivity", "Notification permission granted")
            } else {
                // 권한이 거부되었을 때
                Log.d("MainActivity", "Notification permission denied")
                showPermissionDeniedDialog()
            }
        }
    }
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("알림 권한 필요")
            .setMessage("이 기능을 사용하려면 알림 권한이 필요합니다. 설정에서 권한을 허용해 주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                Firebase.auth.signOut()
//                ActivityStarter.startNextActivity(this,LogInActivity::class.java)
                finish()
            }
            .setNegativeButton("취소") { _, _ ->
                Firebase.auth.signOut()
                ActivityStarter.startNextActivity(this,LogInActivity::class.java)
            }
            .show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }



}


