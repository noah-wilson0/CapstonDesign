package com.example.welfarebenefits.util

import android.widget.CheckBox
import com.example.welfarebenefits.databinding.ActivitySignUpBinding

class CheckedTextExtractor(private val checkBoxes:List<CheckBox>, private val binding: ActivitySignUpBinding) {

    fun getCheckedTexts(): List<String> {

        val checkedTexts = mutableListOf<String>()
        for (checkBox in checkBoxes) {
            if (checkBox.isChecked) {
                checkedTexts.add(checkBox.text.toString())
            }
        }
        return checkedTexts.toList()
    }
}
