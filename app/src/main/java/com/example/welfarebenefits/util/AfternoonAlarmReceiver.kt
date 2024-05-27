
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.welfarebenefits.R
import com.example.welfarebenefits.activity.LogInActivity
import com.example.welfarebenefits.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AfternoonAlarmReceiver : BroadcastReceiver() {
    private lateinit var auth: FirebaseAuth

    override fun onReceive(context: Context, intent: Intent) {
        auth = Firebase.auth

        val notificationManager = NotificationManagerCompat.from(context)

//        val welfareData = WelfareData("Sample Service", "This is a sample service summary")

        val intent = if (auth.currentUser != null) {
            Intent(context, MainActivity::class.java)
        } else {
            Intent(context, LogInActivity::class.java)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notificationicons)
            .setShowWhen(true)
//            .setContentTitle(welfareData.serviceName)
//            .setContentText(welfareData.serviceSummary)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

//        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val CHANNEL_NAME = "channel_name"
        const val NOTIFICATION_ID = 0
    }
}
