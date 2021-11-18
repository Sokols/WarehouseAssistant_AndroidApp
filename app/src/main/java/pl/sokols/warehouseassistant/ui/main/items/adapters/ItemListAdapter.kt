package pl.sokols.warehouseassistant.ui.main.items.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.ItemBinding
import pl.sokols.warehouseassistant.utils.ItemDiffCallback

class ItemListAdapter : ListAdapter<Item, ItemListAdapter.ItemListViewHolder>(
    ItemDiffCallback
) {
    inner class ItemListViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListViewHolder = ItemListViewHolder(
        ItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}