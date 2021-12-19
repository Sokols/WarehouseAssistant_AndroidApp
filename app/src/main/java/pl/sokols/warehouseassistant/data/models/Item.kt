package pl.sokols.warehouseassistant.data.models

import com.google.firebase.database.Exclude

open class BaseItem(
    open var name: String = "",
    open var price: Float = 1.0f
) {

    @get:Exclude
    var id: String = ""
}
