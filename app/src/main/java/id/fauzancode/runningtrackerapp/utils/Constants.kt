package id.fauzancode.runningtrackerapp.utils

import android.graphics.Color

object Constants {
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_STOP = "ACTION_STOP"
    const val ACTION_PAUSE = "ACTION_PAUSE"

    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val RUNNING_DATABASE_NAME = "runs_db"
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"

    const val TIMER_UPDATE_INTERVAL = 50L
}