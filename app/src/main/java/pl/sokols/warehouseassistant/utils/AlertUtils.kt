package pl.sokols.warehouseassistant.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import pl.sokols.warehouseassistant.R

object AlertUtils {

    //region Messaging

    fun showToastMessage(context: Context, msg: String?) {
        msg?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    fun showMessage(
        view: View,
        @StringRes msg: Int,
        @StringRes actionMsg: Int? = null,
        action: ((View) -> Unit)? = null
    ) {
        val bar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        if (actionMsg != null && action != null) {
            bar.setAction(actionMsg, action)
        }
        bar.show()
    }

    fun showAlert(
        context: Context,
        @StringRes title: Int = R.string.app_name,
        @StringRes message: Int,
        @StringRes positiveButton: Int = R.string.ok,
        @StringRes negativeButton: Int? = null,
        positiveClickListener: DialogInterface.OnClickListener? = null,
        negativeClickListener: DialogInterface.OnClickListener? = null
    ) {
        val alert = AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton, positiveClickListener)

        negativeButton?.let {
            alert.setNegativeButton(it, negativeClickListener)
        }
        alert.show()
    }

    fun showAlert(
        context: Context,
        @StringRes title: Int = R.string.app_name,
        message: String?,
        @StringRes positiveButton: Int = R.string.ok,
        @StringRes negativeButton: Int? = null,
        positiveClickListener: DialogInterface.OnClickListener? = null,
        negativeClickListener: DialogInterface.OnClickListener? = null
    ) {
        val alert = AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton, positiveClickListener)

        negativeButton?.let {
            alert.setNegativeButton(it, negativeClickListener)
        }

        alert.show()
    }

    fun showAlert(
        context: Context,
        title: String,
        message: String?,
        @StringRes positiveButton: Int = R.string.ok,
        @StringRes negativeButton: Int? = null,
        positiveClickListener: DialogInterface.OnClickListener? = null,
        negativeClickListener: DialogInterface.OnClickListener? = null
    ) {
        val alert = AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton, positiveClickListener)

        negativeButton?.let {
            alert.setNegativeButton(it, negativeClickListener)
        }

        alert.show()
    }

    //endregion
}