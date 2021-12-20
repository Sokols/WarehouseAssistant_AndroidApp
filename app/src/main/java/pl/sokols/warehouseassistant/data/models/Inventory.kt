package pl.sokols.warehouseassistant.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Inventory(
    var timestampCreated: String = "",
    var timestampEdited: String = "",
    var items: Map<String, CountedItem> = mutableMapOf()
) : Parcelable