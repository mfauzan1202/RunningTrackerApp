package id.fauzancode.runningtrackerapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Runs)

    @Delete
    suspend fun deleteRun(run: Runs)

    @Query("SELECT * FROM runs_table ORDER BY timestamp DESC")
    fun getAllRunsSortedByDate(): LiveData<List<Runs>>

    @Query("SELECT * FROM runs_table ORDER BY timeInMillis DESC")
    fun getAllRunsSortedByTimeInMillis(): LiveData<List<Runs>>

    @Query("SELECT * FROM runs_table ORDER BY caloriesBurned DESC")
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Runs>>

    @Query("SELECT * FROM runs_table ORDER BY avgSpeedInKMH DESC")
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Runs>>

    @Query("SELECT * FROM runs_table ORDER BY distanceInMeters DESC")
    fun getAllRunsSortedByDistance(): LiveData<List<Runs>>

    @Query("SELECT SUM(timeInMillis) FROM runs_table")
    fun getTotalTimeInMillis(): LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM runs_table")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("SELECT SUM(distanceInMeters) FROM runs_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(avgSpeedInKMH) FROM runs_table")
    fun getTotalAvgSpeed(): LiveData<Float>

    @Query("SELECT SUM(distanceInMeters) FROM runs_table WHERE timeStamp=:dates")
    fun getTotalRunByDate(dates: Long): LiveData<Long>
}