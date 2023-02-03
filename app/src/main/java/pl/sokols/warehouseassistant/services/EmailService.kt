package pl.sokols.warehouseassistant.services

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @SuppressLint("IntentReset")
    fun sendEmail(
        recipient: String,
        subject: String? = null,
        message: String? = null,
        attachment: File? = null
    ) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)
        attachment?.let {
            mIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(it))
        }

        try {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            context.startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}