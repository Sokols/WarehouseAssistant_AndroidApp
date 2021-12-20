package pl.sokols.warehouseassistant.utils

import androidx.recyclerview.widget.DiffUtil
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.data.models.Inventory

/**
 * Based on:
 * https://medium.com/@trionkidnapper/recyclerview-more-animations-with-less-code-using-support-library-listadapter-62e65126acdb
 */
object ItemDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is CountedItem && newItem is CountedItem) {
            oldItem.id == newItem.id
        } else if (oldItem is Inventory && newItem is Inventory) {
            oldItem.timestampCreated == newItem.timestampCreated
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is CountedItem && newItem is CountedItem) {
            oldItem == newItem
        } else if (oldItem is Inventory && newItem is Inventory) {
            oldItem == newItem
        } else {
            false
        }
    }
}