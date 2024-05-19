package com.example.welfarebenefits.util

class UserInfoValidator {

    fun validateName(name:String):String{
        return removeNonKoreanAndAlphabet(name)
    }
    fun validateAvgIncome(avgIncome:String):String{
        val avgIncome=removeNonNumeric(avgIncome)
        return avgIncome.replace(",","")
    }
    fun validateHouseHoldSize(houseHoldSize:String):String{
        return removeNonNumeric(houseHoldSize)
    }

    private fun removeNonNumeric(input: String): String { //숫자빼고 모두 삭제
        return input.replace(Regex("[^0-9]"), "")
    }
    private fun removeNonKoreanAndAlphabet(input: String): String { //한글,알파벳빼고 모두 삭제
        return input.replace(Regex("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]"), "")
    }
    fun containsOnlyNumeric(input: String): Boolean { // 숫자만 포함하고 있는지 확인
        return input.matches(Regex("[0-9]+"))
    }
    fun containsOnlyNumericAndSpecial(input: String): Boolean { // 숫자와 ','만 포함하고 있는지 확인
        return input.matches(Regex("[0-9,]+"))
    }

    fun containsOnlyKoreanAndAlphabet(input: String): Boolean { // 한글과 알파벳만 포함하고 있는지 확인
        return input.matches(Regex("[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]+"))
    }

}