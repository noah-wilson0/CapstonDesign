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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.welfarebenefits.R
import com.example.welfarebenefits.databinding.ActivityMainBinding
import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.entity.WelfareCentralAgencyList
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.fragment.CentralAgencyRecyclerviewFragment
import com.example.welfarebenefits.fragment.WelfareUserMatchRecyclerviewFragment
import com.example.welfarebenefits.util.ActivityStarter
import com.example.welfarebenefits.util.Alarm
import com.example.welfarebenefits.util.OnUserInfoClickListener
import com.example.welfarebenefits.util.ToolbarMenuItemClickListener
import com.example.welfarebenefits.util.WelfareDataFetcher
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * 프래그먼트 수정완료 그에 따라 알림도 수정해야 될듯
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityMainBinding?=null
    private val binding get() = mBinding!!
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    private val bundle = Bundle()
    private var id:String=""
    private var spinneritem:String=""
    private lateinit var welfareDataList:List<WelfareData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 알림 권한 요청
        requestNotificationPermission()
        var intent=Intent()
        intent=getIntent()
        if(intent.getStringExtra("id").isNullOrEmpty()){
            id="guest"
        }
        else{
            id= intent.getStringExtra("id")!!
        }
        Log.e("MAIN",id)

        WelfareDataFetcher().getBookmarkDataList(id,this)

        binding.alerm.setOnClickListener {
            Log.e("MAIN","알림시작")
            val welfareList = WelfareCentralAgencyList.getWelfareCentralAgencyList()
            if (welfareList.isNotEmpty()) {
                Alarm(id, this).deliverNotification(welfareList.random())
            } else {
                Log.e("MAIN", "빈 리스트입니다. 알림을 보낼 수 없습니다.")
            }
        }


        binding.toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                return when (item.itemId) {
//                    R.id.searchImage -> {
//                        // 검색 이미지를 클릭한 경우의 동작
//                        Toast.makeText(this@MainActivity, "검색 이미지를 클릭했습니다.", Toast.LENGTH_SHORT)
//                            .show()
//                        return true
//                    }
                    R.id.alertImage -> {

                        ActivityStarter.startNextActivityNotFinish(this@MainActivity,AlertListActivity::class.java,id)
                        return true
                    }
                    R.id.bookmarkImage -> {
                        ActivityStarter.startNextActivityNotFinish(this@MainActivity,Bookmark_listActivity::class.java,id)
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
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id2: Long) {
                val selectedItem = sortingCriteriaArray[position]
                Log.e("MainActivitymainListSelectSpinner",selectedItem)
                position?.let {
                    spinneritem=selectedItem
                }
                bundle.putString("id", id)
                bundle.putString("spinnerItem",spinneritem)
                val fragment = CentralAgencyRecyclerviewFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, fragment) // 수정된 부분: 프래그먼트 변경
                    .commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 이벤트
            }
        }
        binding.main.setOnClickListener(this)
        binding.fit.setOnClickListener(this)
        binding.main.setTypeface(null, Typeface.BOLD)
        binding.fit.setTypeface(null, Typeface.NORMAL)
    }


    override fun onClick(v: View?) {
            when (v?.id) {
                R.id.main -> {
                    binding.main.setTypeface(null, Typeface.BOLD)
                    binding.fit.setTypeface(null, Typeface.NORMAL)
                    Log.e("MAINmain", id)
                    Log.e("MAINmain", spinneritem)
                    bundle.putString("id", id)
                    bundle.putString("spinnerItem", spinneritem)
                    val fragment = CentralAgencyRecyclerviewFragment()
                    fragment.arguments = bundle

                    supportFragmentManager.beginTransaction()
                        .replace(binding.frameLayout.id, fragment)
                        .commit()
                }
                R.id.fit -> {
                    binding.main.setTypeface(null, Typeface.NORMAL)
                    binding.fit.setTypeface(null, Typeface.BOLD)
                    Log.e("MAINfit",id)
                    Log.e("MAINfit",spinneritem)
                    bundle.putString("id", id)
                    bundle.putString("spinnerItem", spinneritem)
                    val fragment = WelfareUserMatchRecyclerviewFragment()
                    fragment.arguments = bundle

                    supportFragmentManager.beginTransaction()
                        .replace(binding.frameLayout.id, fragment)
                        .commit()
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


