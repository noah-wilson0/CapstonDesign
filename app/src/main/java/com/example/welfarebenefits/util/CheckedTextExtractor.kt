package com.example.welfarebenefits.util

import android.widget.CheckBox
import com.example.welfarebenefits.databinding.ActivitySignUpBinding

class CheckedTextExtractor(private val checkBoxes:MutableList<CheckBox>, private val binding: ActivitySignUpBinding) {

    fun getCheckedTexts(): MutableList<String?> {

        val checkedTexts = mutableListOf<String>()
        for (checkBox in checkBoxes) {
            if (checkBox.isChecked) {
                checkedTexts.add(checkBox.text.toString())
            }
        }
        return checkedTexts.toMutableList()
    }
}
