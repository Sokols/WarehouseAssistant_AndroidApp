package pl.sokols.warehouseassistant.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Inventory(
    var timestampId: String,
    var items: List<CountedItem>? = null
) : Parcelable