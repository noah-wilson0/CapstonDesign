package com.example.welfarebenefits.util
/*
각각의 중위소득액은 중위소득 100%를 기준으로 해서 산출한다.
예를 중위소득 75% 값을 얻고자 한다면 가구 중위소득액 100%값 X 0.75로 계산하면 한다.
 */
class MonthSalaryConverter {
    fun convertToMedianIncome(income: String, familyMembers: String): String {
        var result:String=""
        // 1. 입력값 검증
        if (income.isNotBlank() && familyMembers.isNotBlank()) {
            // 2. 변수 선언
            val monthlyIncome = income.toInt() // 월급을 문자열에서 정수로 변환
            val familyMembersCount = familyMembers.toInt() // 가구원 수를 문자열에서 정수로 변환

            // 3. 기준 중위소득 데이터 불러오기 (예시: 파일, API, 데이터베이스)
            val medianIncomeData = loadMedianIncomeData() // 가상 함수

            // 4. 가구원 수에 따른 기준 중위소득 찾기
            val baseMedianIncome = medianIncomeData[familyMembersCount - 1] // 가구원 수에 해당하는 기준 중위소득

            // 5. 소득 수준 계산
            val incomeRatio = (monthlyIncome / baseMedianIncome!!) * 100

            // 6. 결과 문자열 생성
            result = when (incomeRatio) {
                in 0..49 -> "50%"
                in 50..59 -> "60%"
                in 60..69 -> "70% "
                in 70..79 -> "80%"
                in 80..89 -> "90%"
                in 90..99 -> "100%"
                in 100..109 -> "110%"
                in 110..119 -> "120%"
                in 120..129 -> "130%"
                in 130..139 -> "140%"
                else -> "150%"
            }
        }
        return result
    }
    private fun loadMedianIncomeData(): Map<Int, Int> {
        // 실제 데이터 불러오기 로직 작성
        return mapOf(
            1 to 2228445,
            2 to 3682609,
            3 to 4714657,
            4 to 5729913,
            5 to 6695735,
            6 to 7618369
        )
    }
}