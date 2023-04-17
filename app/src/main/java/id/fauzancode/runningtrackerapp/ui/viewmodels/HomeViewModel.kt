package id.fauzancode.runningtrackerapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.fauzancode.runningtrackerapp.db.Statistics
import id.fauzancode.runningtrackerapp.repositories.MainRepository
import id.fauzancode.runningtrackerapp.utils.TrackingUtils
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class HomeViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    val totalTimeRun = mainRepository.getTotalTimeInMillis()
    val totalDistance = mainRepository.getTotalDistance()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()
//    fun getTotalStatistics() : LiveData<List<Statistics>> {
//        val listStatistics = mutableListOf<Statistics>()
//        val list = MutableLiveData<List<Statistics>>()
//        totalTimeRun.value?.let {
//            val totalTimeRun = Statistics("Total Time Run", TrackingUtils.getFormattedStopWatchTime(it))
//            listStatistics.add(totalTimeRun)
//        }
//        totalDistance.value?.let {
//            val km = it / 1000f
//            val totalDistance = Statistics("Total Distance", "${round(km * 10f) / 10f}")
//            listStatistics.add(totalDistance)
//        }
//        totalCaloriesBurned.value?.let {
//            val totalCaloriesBurned = Statistics("Total Calories Burned", it.toString())
//            listStatistics.add(totalCaloriesBurned)
//        }
//        totalAvgSpeed.value?.let {
//            val avgSpeedString = "${round(it * 10f) / 10f}"
//            val totalAvgSpeed = Statistics("Total Avg Speed", avgSpeedString)
//            listStatistics.add(totalAvgSpeed)
//        }
//        return list.apply { value = listStatistics }
//    }
}