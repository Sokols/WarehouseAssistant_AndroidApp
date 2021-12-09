package pl.sokols.warehouseassistant.utils

import androidx.recyclerview.widget.DiffUtil
import pl.sokols.warehouseassistant.data.models.Item

/**
 * Based on:
 * https://medium.com/@trionkidnapper/recyclerview-more-animations-with-less-code-using-support-library-listadapter-62e65126acdb
 */
object ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}