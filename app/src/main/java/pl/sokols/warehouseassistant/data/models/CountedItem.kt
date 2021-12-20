package pl.sokols.warehouseassistant.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountedItem(
    override var name: String = "",
    override var price: Float = 1.0f,
    var amount: Int = 1,
    var difference: Int? = null
) : BaseItem(), Parcelable {

    fun copy(
        id: String = this.id,
        name: String = this.name,
        price: Float = this.price,
        amount: Int = this.amount,
        difference: Int? = this.difference
    ): CountedItem {
        val copy = CountedItem(name = name, price = price, amount = amount, difference = difference)
        copy.id = id
        return copy
    }
}