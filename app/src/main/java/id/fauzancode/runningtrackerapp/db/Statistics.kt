package id.fauzancode.runningtrackerapp.db

import androidx.room.PrimaryKey


data class Statistics (
    val Title: String,
    val Value: String,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}