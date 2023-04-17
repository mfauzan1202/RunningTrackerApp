package id.fauzancode.runningtrackerapp.utils

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.db.Runs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomMarkerView(
    val runs: List<Runs>,
    context: Context,
    layoutId: Int
) : MarkerView(context, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)

        if (e == null) {
            return
        }
        val curRun = e.x.toInt()
        val run = runs[curRun]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timeStamp
        }

        val tvDate = findViewById<TextView>(R.id.tv_mvDate)
        val tvAvgSpeed = findViewById<TextView>(R.id.tv_mvAvgSpeed)
        val tvDistance = findViewById<TextView>(R.id.tv_mvDistance)
        val tvDuration = findViewById<TextView>(R.id.tv_mvDuration)
        val tvCaloriesBurned = findViewById<TextView>(R.id.tv_mvCaloriesBurned)

        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        tvDate.text = dateFormat.format(calendar.time)

        val avgSpeed = "${run.avgSpeedInKMH}km/h"
        tvAvgSpeed.text = avgSpeed

        val distanceInKm = "${run.distanceInMeters / 1000f}km"
        tvDistance.text = distanceInKm

        tvDuration.text = TrackingUtils.getFormattedStopWatchTime(run.timeInMillis)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        tvCaloriesBurned.text = caloriesBurned

    }
}