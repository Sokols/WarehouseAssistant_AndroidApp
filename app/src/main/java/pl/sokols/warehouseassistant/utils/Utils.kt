package pl.sokols.warehouseassistant.utils

import android.annotation.SuppressLint
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object Utils {

    @SuppressLint("NewApi")
    fun getTimestamp(): String = DateTimeFormatter
        .ofPattern("yyyy-MM-dd_HH:mm:ss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())
        .toString()
}