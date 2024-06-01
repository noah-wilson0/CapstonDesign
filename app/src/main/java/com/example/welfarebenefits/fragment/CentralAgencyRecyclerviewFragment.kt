package com.example.welfarebenefits.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfarebenefits.R
import com.example.welfarebenefits.activity.SubListActivity
import com.example.welfarebenefits.adapter.OnItemClickListener
import com.example.welfarebenefits.adapter.RecyclerViewAdapter
import com.example.welfarebenefits.databinding.FragmentCentralAgencyRecyclerviewBinding
import com.example.welfarebenefits.entity.WelfareCentralAgencyMap
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.AlarmScheduler
import com.example.welfarebenefits.util.BookmarkUpdater
import com.example.welfarebenefits.util.CallBackWelfareData
import com.example.welfarebenefits.util.JsonConverter
import com.example.welfarebenefits.util.WelfareDataFetcher

class CentralAgencyRecyclerviewFragment : Fragment(), OnItemClickListener {
    private var mBinding:FragmentCentralAgencyRecyclerviewBinding?=null
    private val binding get() = mBinding!!
    private var id:String=""
    private var spinnerItem: String=""
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { bundle ->
            id= bundle.getString("id")!!
            spinnerItem= bundle.getString("spinnerItem")!!
        }
        WelfareDataFetcher().fetchCentralAgencyWelfareData(object :CallBackWelfareData{
            override fun getWelfareData(welfareDataList: List<WelfareData>) {
                recyclerViewAdapter = if (spinnerItem !=
                    resources.getStringArray(R.array.sorting_criteria)[0]) {
                    WelfareCentralAgencyMap.getCategoryMap(spinnerItem)?.let {
                        RecyclerViewAdapter(it, this@CentralAgencyRecyclerviewFragment)
                    }

                } else {
                    RecyclerViewAdapter(welfareDataList,
                        this@CentralAgencyRecyclerviewFragment)
                }
                Log.e("CentralAgencyRecyclerviewFragment",
                    recyclerViewAdapter!!.itemCount.toString())
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = recyclerViewAdapter

                val alarmScheduler = AlarmScheduler(requireContext())
                welfareDataList.let {
                    alarmScheduler.scheduleDailyAlarm( resources.getInteger(R.integer.agencyHour), resources.getInteger(
                        R.integer.agencyMinute),id,welfareDataList[(welfareDataList.indices).random()])
                }
            }

        })
    }
     override fun onResume(){
         super.onResume()
         Log.e("onResumeMainActivity", "메인액티비티 resume")
         recyclerViewAdapter?.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCentralAgencyRecyclerviewBinding.inflate(layoutInflater)

        return binding.root

    }
    override fun onItemClick(position: Int) {
         val clickedItem = binding.recyclerView.adapter?.let { adapter ->
             if (adapter is RecyclerViewAdapter) {
                 adapter.getItem(position)
             } else null
         }
         clickedItem?.let {
             val intent = Intent(requireActivity(), SubListActivity::class.java).apply {
                 putExtra("DATA", JsonConverter().dataToJson(it))
             }
             startActivity(intent)
         }
      }

         override fun onButtonClick(position: Int) {
         val clickedBtn = binding.recyclerView.adapter?.let { adapter ->
             if (adapter is RecyclerViewAdapter) {
                 adapter.getItem(position)
             } else null
         }
         clickedBtn?.let {
             BookmarkUpdater().updateBookmark(id,it)
         }
     }

}