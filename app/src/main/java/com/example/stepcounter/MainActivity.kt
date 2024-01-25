package com.example.stepcounter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.stepcounter.Const.Actions
import com.example.stepcounter.ui.theme.StepCounterTheme
import com.example.stepcounter.ui.theme.TextColorInsedeCircle
import com.example.stepcounter.ui.theme.colorBehind
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import org.w3c.dom.Text
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {




//    val mainViewModel by viewModels<MainViewModel>()


    @Inject
    lateinit var mainViewModel: MainViewModel

//    val mainViewModel: MainViewModel = hiltViewModel()
//    val mainViewModel2: MainViewModel = MainViewModel()

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.POST_NOTIFICATIONS),
            0)
    }





    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    if (stepSensor != null) {
        Intent(this@MainActivity, StepCounterService::class.java).also {
            it.action = Actions.START.toString()
            it.putExtra("sensorManager", sensorManager != null)
            it.putExtra("stepSensorAvailable", stepSensor != null)
            this@MainActivity.startForegroundService(it)
        }
    } else {
        Toast.makeText(this, "На вашем устройстве нет датчика шагов", Toast.LENGTH_LONG).show()
    }


        setContent {
            StepCounterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val steps = mainViewModel.numberOfSteps.collectAsState()
                    val desiredAmountOfSteps = mainViewModel.desiredStepsAmount
                    val totalStepsFromVM = mainViewModel.totalStepsVM.collectAsState()
                    val previousTotalSteps = mainViewModel.previousTotalStepsVM.collectAsState()

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        val oneDegreeStepWise = desiredAmountOfSteps.toFloat() / 360f
                        val totalStepsToDegrees = steps.value.toFloat() / oneDegreeStepWise

                        Text(
                            text = "Total steps of the device is  = ${totalStepsFromVM.value}",
                        )
                        Text(
                            text = "Количество шагов  = ${steps.value}",
                        )

                        Log.d("StepCouLog", "Количество шагов  = ${steps.value}")

                        Box(modifier = Modifier
//                            .padding(100.dp)
                            .sizeIn(1.dp,4.dp)

                        ){
                            DrawCircle(degreesPassed = totalStepsToDegrees, modifier = Modifier.align(Alignment.Center))

                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = " ${steps.value} / $desiredAmountOfSteps",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = TextColorInsedeCircle,
//                                    fontSize = Ma
                                )
                                Text(
                                    text = "шагов",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.DarkGray

                                    )
                            }

                        }




                        
                        if (steps.value > 0){
                            Spacer(modifier = Modifier.height(15.dp))
                            Button(onClick = {
                                mainViewModel.resetSteps()
                            }) {
                                Text(text = "Обнулить шаги")
                            }
                        }


                        Spacer(modifier = Modifier.height(40.dp))
                        Slider(
                            modifier = Modifier.padding(horizontal = 20.dp),

                            value = mainViewModel.desiredStepsAmount.toFloat(),
                            valueRange = 5000f..15000f,
                            steps = 9,
                            onValueChange = {

                                mainViewModel.changeDesiredStepsAmount(it.toInt())

                            }
                        )


                        Text(text = "Установите целевое количество шагов", color = Color.Gray)

                    }







                }
            }
        }
    }







//    override fun onResume() {
//        super.onResume()
//        running = true
//
//        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        if (stepSensor == null) {
//            Toast.makeText(this, "Step counter sensor not available", Toast.LENGTH_LONG).show()
//
//        } else {
//            Toast.makeText(this, "Step counter sensor is available", Toast.LENGTH_LONG).show()
////            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
//            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
//        }
//
//
//
//    }
//
//    override fun onSensorChanged(event: SensorEvent?) {
//        if (running){
//            totalSteps = event!!.values[0]
//            mainViewModel.changeTotalStepsNumber(totalSteps.toInt())
//
//            if (!ifPreviousStepsUpdated){
//                previousTotalSteps = totalSteps
//                mainViewModel.changePreviousTotalStepsNumber(totalSteps.toInt())
//                ifPreviousStepsUpdated = true
//            }
//
//            val currentSteps = mainViewModel.totalStepsVM.value - mainViewModel.previousTotalStepsVM.value
//
//            mainViewModel.changeStepsNumber(currentSteps)
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//
//    }
//
//
//    private fun resetSteps(){
//
//
//        mainViewModel.changeStepsNumber(102)
//        ifPreviousStepsUpdated = false
//
//
//
//
//    }
//
//
//    private fun saveData(){
//    val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putFloat("key1", previousTotalSteps)
//        editor.apply()
//    }
//
//    private fun loadData(){
//        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
//        val savedNumber = sharedPreferences.getFloat("key1",0f)
//        Log.d("MainActivity", "$savedNumber")
//        previousTotalSteps = savedNumber
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onStop() {
//        super.onStop()
//
//
//    }





}






@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StepCounterTheme {
        Greeting("Android")
    }
}