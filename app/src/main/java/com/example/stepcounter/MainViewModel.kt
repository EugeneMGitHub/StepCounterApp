package com.example.stepcounter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

): ViewModel() {

    private val _numberOfSteps = MutableStateFlow<Int>(8000)
    val numberOfSteps = _numberOfSteps.asStateFlow()

    private  val _totalStepsVM = MutableStateFlow<Int>(0)
    val totalStepsVM = _totalStepsVM.asStateFlow()


    private  val _previousTotalStepsVM = MutableStateFlow<Int>(0)
    val previousTotalStepsVM  = _previousTotalStepsVM.asStateFlow()

    private  val _previouslyUpdated = MutableStateFlow<Boolean>(false)
    val previouslyUpdated  = _previouslyUpdated.asStateFlow()

    var isServiceRuninng by mutableStateOf(false)
        private set

   var desiredStepsAmount by mutableStateOf(6000)
       private set


    fun changeDesiredStepsAmount(newValue: Int){
        desiredStepsAmount = newValue
    }

    fun changeIfServiceRunning(newValue: Boolean){
        isServiceRuninng = newValue
    }



    fun changePpreviouslyUpdated(newValue: Boolean){
        _previouslyUpdated.value = newValue
    }


    fun changeStepsNumber(newValue: Int){
        _numberOfSteps.value = newValue
    }

    fun changeTotalStepsNumber(newValue: Int){
        _totalStepsVM.value = newValue
    }

    fun changePreviousTotalStepsNumber(newValue: Int){
        _previousTotalStepsVM.value = newValue
    }



   fun resetSteps(){
        changeStepsNumber(0)
//        changePreviousTotalStepsNumber(previousTotalStepsVM.value - numberOfSteps.value)
        changePpreviouslyUpdated(false)
    }


}