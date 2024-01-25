package com.example.stepcounter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.CaseMap.Title
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import com.example.stepcounter.Const.Actions
import com.example.stepcounter.Const.Const
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService : Service(), SensorEventListener
{

    @Inject
    lateinit var mainViewModel: MainViewModel



    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    val channelId = Const.CHANNEL_FOR_STEP_COUNTER_SERVICE
    val channelName = Const.CHANNEL_FOR_STEP_COUNTER_SERVICE
    val foregroundServiceId = Const.CHANNEL_ID_FOR_STEP_COUNTER_SERVICE
    val notificationTitle = Const.NOTIFICATION_TITLE




    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null

    private var totalSteps = 0f
    private var previousTotalSteps = 0f


    override fun onSensorChanged(event: SensorEvent?) {

        Toast.makeText(this, "onSensorChanged: Something happened", Toast.LENGTH_LONG).show()

            totalSteps = event!!.values[0]
            mainViewModel.changeTotalStepsNumber(7777777)

//        CoroutineScope(Dispatchers.Main).launch {
//            mainViewModel.changeTotalStepsNumber(7777777)
//        }

//
//        GlobalScope.launch(Dispatchers.Main) {
//            mainViewModel.changeTotalStepsNumber(7777777)
//        }



//
//            if (!mainViewModel.previouslyUpdated.value){
//                previousTotalSteps = totalSteps
//                mainViewModel.changePreviousTotalStepsNumber(totalSteps.toInt())
//                mainViewModel.changePpreviouslyUpdated(true)
//            }
//
//            val currentSteps = mainViewModel.totalStepsVM.value - mainViewModel.previousTotalStepsVM.value
//
//            mainViewModel.changeStepsNumber(currentSteps)



    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }






    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        // Retrieve the SensorManager and stepSensor information from the intent
        val sensorManagerAvailable = intent?.getBooleanExtra("sensorManager", false)
        val stepSensorAvailable = intent?.getBooleanExtra("stepSensorAvailable", false)

//        mainViewModel.changeTotalStepsNumber(999)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)



        if (stepSensor != null) {
            Toast.makeText(this, "Сенсор в сервисе есть", Toast.LENGTH_LONG).show()
        }


        when(intent?.action){
            Actions.START.toString() -> {
                val channelId = createChannel(channelId = this.channelId, channelName = this.channelName)
                startForegroundServiceWithNotification(channelId = channelId, notificationTitle = notificationTitle, notificationText = "Сервис подсчета шагов работает")

//                if (!isNotificationShown()) {
//                    startForegroundServiceWithNotification(
//                        channelId = channelId,
//                        notificationTitle = notificationTitle,
//                        notificationText = "Сервис подсчета шагов работает"
//                    )
//                    markNotificationAsShown()
//                }



//                if (mainViewModel.isServiceRuninng == false) {
//                    startForegroundServiceWithNotification(channelId = channelId, notificationTitle = notificationTitle, notificationText = "Сервис подсчета шагов работает")
//                    mainViewModel.changeIfServiceRunning(true)
//                }

            }
            Actions.STOP.toString() -> {
                startForegroundServiceWithNotification(channelId = channelId, notificationTitle = notificationTitle, notificationText = "Сервис подстчета шагов остановлен")
                scope.launch {
                    delay(5000L)
                }
                stopSelf()
            }
        }



        return super.onStartCommand(intent, flags, startId)




    }






    private fun createChannel(channelId: String, channelName: String) : String{
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                Const.CHANNEL_FOR_STEP_COUNTER_SERVICE,
                Const.CHANNEL_FOR_STEP_COUNTER_SERVICE
            )
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            ""
        }

        return channelId
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return channelId
    }


    private fun startForegroundServiceWithNotification(
        channelId: String,
        notificationTitle: String = this.notificationTitle,
        notificationText: String
    ){

            val notification = createNotification(channelId = channelId, notificationTitle = notificationTitle, notificationText = notificationText)
            startForeground(foregroundServiceId, notification)

    }

    private fun createNotification(channelId: String, notificationTitle: String, notificationText: String): Notification {
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
        val notification = notificationBuilder
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .build()
        return notification

    }


    private fun markNotificationAsShown() {
        val sharedPreferences = getSharedPreferences(channelId, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(channelId, true)
        editor.apply()
    }

    private fun isNotificationShown(): Boolean {
        val sharedPreferences = getSharedPreferences(channelId, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(channelId, false)
    }

}