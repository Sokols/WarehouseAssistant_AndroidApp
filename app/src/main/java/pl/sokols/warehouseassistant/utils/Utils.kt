package pl.sokols.warehouseassistant.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import pl.sokols.warehouseassistant.R
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

    fun displayLogoutDialog(
        context: Context,
        question: String,
        positiveCallback: DialogInterface.OnClickListener
    ): AlertDialog.Builder =
        AlertDialog.Builder(context)
            .setTitle(question)
            .setPositiveButton(context.getString(R.string.yes), positiveCallback)
            .setNegativeButton(context.getString(R.string.no)) { _, _ -> }
}