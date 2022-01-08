package pl.sokols.warehouseassistant.data.models

import com.google.firebase.database.Exclude
import com.google.gson.annotations.SerializedName

open class BaseItem(
    @SerializedName("item_name")
    open var name: String = "",
    @SerializedName("item_price")
    open var price: Float = 1.0f
) {
    @get:Exclude
    @SerializedName("item_id")
    var id: String = ""
}
