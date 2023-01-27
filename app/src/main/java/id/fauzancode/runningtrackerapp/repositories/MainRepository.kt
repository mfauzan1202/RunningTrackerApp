package id.fauzancode.runningtrackerapp.repositories

import id.fauzancode.runningtrackerapp.db.RunDao
import id.fauzancode.runningtrackerapp.db.Runs
import javax.inject.Inject

class MainRepository @Inject constructor(val runDao: RunDao) {
    suspend fun insertRun(run: Runs) = runDao.insertRun(run)

    suspend fun deleteRun(run: Runs) = runDao.deleteRun(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()

    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()

    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()
}