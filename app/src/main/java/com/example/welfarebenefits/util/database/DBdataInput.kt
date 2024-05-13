package com.example.welfarebenefits.util.database


import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.example.welfarebenefits.entity.WelfareData
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

fun main() {
    var welfareData:WelfareData     // 읽어온 데이터에서 필요한 데이터만 객체화
    val gson = GsonBuilder().setPrettyPrinting().create()               // json 파싱을 위한 gson 라이브러리
    var url:URL                     // 오픈 api 주소
    var readString:String           // 오픈 api에서 읽어온 데이터
    var readJsonObject:JsonObject   // readString를 JsonObject로 파싱
    var dataElements:JsonElement    // readJsonObject에서 "data"부분만 추출
    var dataJsonArray:JsonArray     // dataElements를 JsonArray로 파싱 -- jsonElement는 요소라서 그 배열값에 접근 불가능
    var welfareElement:JsonElement  // 데이터 json
    var jsonObject:JsonObject       // 서비스명:데이터 json
    var jsonString:String=""        // 출력을 위한 jsonString

    var serviceName : String                    // 서비스명
    var serviceCategory : String                // 카테고리
    val livingStabilityJson = JsonObject()       // 생활환경
    val residenceJson = JsonObject()            // 주거 자립
    val childcareJson = JsonObject()            // 보육 교육
    val employmentJson = JsonObject()           // 고용 창업
    val medicalJson = JsonObject()              // 보건 의료
    val administrationJson = JsonObject()       // 행정 안전
    val pregnancyJson = JsonObject()            // 임신 출산
    val protectionJson = JsonObject()           // 보호 돌봄
    val cultureJson = JsonObject()              // 문화 환경
    var count = 1
    for(i in 1..10){
        url = URL("https://api.odcloud.kr/api/gov24/v3/serviceList?page="+i+"&perPage=873&cond%5B%EC%82%AC%EC%9A%A9%EC%9E%90%EA%B5%AC%EB%B6%84%3A%3ALIKE%5D=%EA%B0%9C%EC%9D%B8&serviceKey=Ush%2BtsMQ5k9gtfDbu%2BU0AwuYGCv5OA5SHbOtNWhpfAmK1sTJuDtkGPszEl%2Feyfy2ABxJ%2FL0iELAkM7bvfUXHBA%3D%3D") // Open API URL 입력
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET" // GET 요청 설정
        connection.connect() // 서버 연결

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            readString = bufferedReader.readText() // JSON 문자열 읽기
            bufferedReader.close()

            // JSON 파싱 (Gson 사용)
            readJsonObject = gson.fromJson(readString, JsonObject::class.java)  // api에서 읽어온 string to json
            dataElements = readJsonObject["data"]                               // 필요한 data 부분만 추출
            dataJsonArray = gson.fromJson(dataElements, JsonArray::class.java)  // data 부분은 array라서 파싱
            for (data in dataJsonArray) {                                       // foreach문 사용
                jsonObject = data.asJsonObject                                  // json배열의 요소는 jsonElement라서 jsonObject로 파싱

                welfareData = WelfareData(                                      // 저장을 위한 welfare객체 생성
                    jsonObject["상세조회URL"].asString,
                    jsonObject["서비스ID"].asString,
                    jsonObject["서비스명"].asString,
                    if(jsonObject["서비스목적요약"].isJsonNull) "" else jsonObject["서비스목적요약"].asString,
                    if(jsonObject["선정기준"].isJsonNull) "" else jsonObject["선정기준"].asString,
                    jsonObject["신청기한"].asString,
                    jsonObject["신청방법"].asString,
                    jsonObject["지원내용"].asString
                )
                serviceName = jsonObject["서비스명"].asString        // key값 저장
                serviceCategory = jsonObject["서비스분야"].asString
                welfareElement = gson.toJsonTree(welfareData)       // object에 키와 밸류값을 저장하기 위해 element로 파싱
                when(serviceCategory) {
                    "생활안정" -> {
                        livingStabilityJson.add(serviceName,welfareElement)
                        println(count++.toString()+"생활안정 추가")
                    }
                    "주거·자립" -> {
                        residenceJson.add(serviceName,welfareElement)
                        println(count++.toString()+"주거자립 추가")
                    }
                    "보육·교육" ->{
                        childcareJson.add(serviceName,welfareElement)
                        println(count++.toString()+"보육교육 추가")
                    }
                    "고용·창업" -> {
                        employmentJson.add(serviceName,welfareElement)
                        println(count++.toString()+"고용창업 추가")
                    }
                    "보건·의료" -> {
                        medicalJson.add(serviceName,welfareElement)
                        println(count++.toString()+"보건의료 추가")
                    }
                    "행정·안전" -> {
                        administrationJson.add(serviceName,welfareElement)
                        println(count++.toString()+"행정안전 추가")
                    }
                    "임신·출산" -> {
                        pregnancyJson.add(serviceName,welfareElement)
                        println(count++.toString()+"임신출산 추가")
                    }
                    "보호·돌봄" -> {
                        protectionJson.add(serviceName,welfareElement)
                        println(count++.toString()+"보호돌봄 추가")
                    }
                    "문화·환경" -> {
                        cultureJson.add(serviceName,welfareElement)
                        println(count++.toString()+"문화환경 추가")
                    }
                }
            }
        }else {
                println("Error: ${connection.responseCode}")
            }

        connection.disconnect() // 서버 연결 해제

    }
    val livingStabilityJsons = JsonObject()       // 생활환경
    val residenceJsons = JsonObject()            // 주거 자립
    val childcareJsons = JsonObject()            // 보육 교육
    val employmentJsons = JsonObject()           // 고용 창업
    val medicalJsons = JsonObject()              // 보건 의료
    val administrationJsons = JsonObject()       // 행정 안전
    val pregnancyJsons = JsonObject()            // 임신 출산
    val protectionJsons = JsonObject()           // 보호 돌봄
    val cultureJsons = JsonObject()              // 문화 환경
    livingStabilityJsons.add("생활안정",livingStabilityJson)
    residenceJsons.add("주거·자립", residenceJson)
    childcareJsons.add("보육·교육", childcareJson)
    employmentJsons.add("고용·창업", employmentJson)
    medicalJsons.add("보건·의료", medicalJson)
    administrationJsons.add("행정·안전", administrationJson)
    pregnancyJsons.add("임신·출산", pregnancyJson)
    protectionJsons.add("보호·돌봄", protectionJson)
    cultureJsons.add("문화·환경", cultureJson)
    println(gson.toJson(residenceJsons))
    val outputArray = arrayOf(livingStabilityJsons,residenceJsons,childcareJsons,employmentJsons,medicalJsons,administrationJsons,pregnancyJsons,protectionJsons,cultureJsons)
    val fileNameArray = arrayOf("생활안정","주거·자립","보육·교육","고용·창업","보건·의료","행정·안전","임신·출산","보호·돌봄","문화·환경")
    for(i in 0..outputArray.size-1){
        val path = "C:\\Users\\Hanseo\\Desktop\\welfareDataFullEX.json"
        val file = File(path)
        val bufferedWriter = BufferedWriter(FileWriter(file,true))
        bufferedWriter.write(gson.toJson(outputArray[i]))
        bufferedWriter.write("\n,")
        bufferedWriter.close()
    }


}
