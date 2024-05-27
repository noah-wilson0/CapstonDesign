
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

/**https://stickode.tistory.com/621 에
 * Intent receiverIntent = new Intent(this, NotificationReceiver.class);
 *       receiverIntent.putExtra("content", "알람등록 테스트");
 *       을 참고하여
 *        alarmIntent.putExtra("welfareData",)을 완성해보기
 *        Manager에 data를 주입시키려면 어플이 런타임이어야하는데 어떻게 가능하지?
 */
class AlarmManager(private val context: Context) {
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm() {
        val alarmIntent = Intent(context, AlarmManager::class.java)
//        alarmIntent.putExtra("welfareData",)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 17) // 오후 5시
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            // 현재 시간보다 이전일 경우 다음날로 설정
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
