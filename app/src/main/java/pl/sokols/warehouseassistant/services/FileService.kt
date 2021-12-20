package pl.sokols.warehouseassistant.services

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Inventory
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun generateTextFile(inventory: Inventory): File? {
        var file: File? = null
        try {
            file = File(context.getExternalFilesDir(null), inventory.timestampCreated + ".txt")
            file.writeText(Gson().toJson(inventory))
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
        return file
    }
}