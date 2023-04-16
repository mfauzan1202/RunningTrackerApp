package id.fauzancode.runningtrackerapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs_table")
data class Runs(
    var img: Bitmap? = null,
    var timeStamp: Long = 0L,

    var timeInMillis: Long = 0L,//total time in one run
    var caloriesBurned: Int = 0,//total calories burned in one run
    var avgSpeedInKMH: Float = 0f,//average speed in one run
    var distanceInMeters: Int = 0,//total distance in one run
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}