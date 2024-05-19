package com.example.welfarebenefits.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CityDoAdapter(context: Context, cityDoArray: Array<String>) :
    ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,cityDoArray) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view as TextView
        textView.text = getItem(position)
        return view
    }
}
