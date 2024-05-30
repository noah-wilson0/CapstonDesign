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
import com.example.welfarebenefits.databinding.FragmentWelfareUserMatchRecyclerviewBinding
import com.example.welfarebenefits.entity.WelfareCategoryMap
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.AlarmScheduler
import com.example.welfarebenefits.util.CallBackWelfareData
import com.example.welfarebenefits.util.WelfareDataFetcher


class WelfareUserMatchRecyclerviewFragment : Fragment(), OnItemClickListener {
    private var mBinding:FragmentWelfareUserMatchRecyclerviewBinding?=null
    private val binding get() = mBinding!!
    private var id:String=""
    private var spinnerItem: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var recyclerViewAdapter: RecyclerViewAdapter? = null

        arguments?.let { bundle ->
            id= bundle.getString("id")!!
            spinnerItem= bundle.getString("spinnerItem")!!
        }
        Log.e("WelfareUserMatchRecyclerviewFragment",id)
        if (id=="guest") {
            WelfareDataFetcher().getWelfareDataGUEST(object : CallBackWelfareData {
                override fun getWelfareData(welfareDataList: List<WelfareData>) {
                    Log.e("MainActivity", "Received welfare data: ${welfareDataList.size} items")
                    recyclerViewAdapter = if (spinnerItem != resources.getStringArray(R.array.sorting_criteria)[0]) {
                        WelfareCategoryMap.getCategoryMap(spinnerItem)?.let { RecyclerViewAdapter(it, this@WelfareUserMatchRecyclerviewFragment) }
                    } else {
                        RecyclerViewAdapter(welfareDataList, this@WelfareUserMatchRecyclerviewFragment)
                    }
                    Log.e("WelfareUserMatchRecyclerviewFragment",
                        recyclerViewAdapter!!.itemCount.toString())

                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = recyclerViewAdapter

                    val alarmScheduler = AlarmScheduler(requireContext())
                    welfareDataList.let {
                        alarmScheduler.scheduleDailyAlarm( resources.getInteger(R.integer.userFitHour),
                            resources.getInteger(R.integer.userFitMinute),id,welfareDataList[(welfareDataList.indices).random()])
                    }
                }
            })
        }
        else {
            WelfareDataFetcher().getWelfareData(requireContext(), id, object : CallBackWelfareData {
                override fun getWelfareData(welfareDataList: List<WelfareData>) {
                    Log.e("MainActivity", "Received welfare data: ${welfareDataList.size} items")
                    recyclerViewAdapter = if (spinnerItem != resources.getStringArray(R.array.sorting_criteria)[0]) {
                        WelfareCategoryMap.getCategoryMap(spinnerItem)?.let { RecyclerViewAdapter(it, this@WelfareUserMatchRecyclerviewFragment) }
                    } else {
                        RecyclerViewAdapter(welfareDataList, this@WelfareUserMatchRecyclerviewFragment)
                    }
                    Log.e("WelfareUserMatchRecyclerviewFragment",
                        recyclerViewAdapter!!.itemCount.toString())
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = recyclerViewAdapter

                    val alarmScheduler = AlarmScheduler(requireContext())
                    welfareDataList.let {
                        alarmScheduler.scheduleDailyAlarm(
                            resources.getInteger(R.integer.userFitHour), resources.getInteger(
                                R.integer.userFitMinute
                            ), id, welfareDataList[(welfareDataList.indices).random()]
                        )
                    }
                }
            })

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentWelfareUserMatchRecyclerviewBinding.inflate(layoutInflater)

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

    override fun onButtonClick(position: Int) {
        TODO("Not yet implemented")
    }

}