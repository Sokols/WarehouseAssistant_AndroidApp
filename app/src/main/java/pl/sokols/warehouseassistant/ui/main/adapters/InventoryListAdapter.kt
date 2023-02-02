package pl.sokols.warehouseassistant.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.InventoryItemBinding

class InventoryListAdapter(
    private val onItemClick: (Inventory) -> Unit
) : ListAdapter<Inventory, InventoryListAdapter.InventoryListViewHolder>(ItemDiffCallback) {

    inner class InventoryListViewHolder(
        private val binding: InventoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(inventory: Inventory) {
            binding.apply {
                this.inventory = inventory
                itemLayout.setOnClickListener { onItemClick(inventory) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryListViewHolder = InventoryListViewHolder(
        InventoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: InventoryListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //region DiffCallback

    object ItemDiffCallback : DiffUtil.ItemCallback<Inventory>() {
        override fun areItemsTheSame(oldItem: Inventory, newItem: Inventory): Boolean {
            return oldItem.timestampCreated == newItem.timestampCreated
        }

        override fun areContentsTheSame(oldItem: Inventory, newItem: Inventory): Boolean {
            return oldItem.timestampCreated == newItem.timestampCreated
                    && oldItem.timestampEdited == newItem.timestampEdited
                    && oldItem.items == newItem.items
        }
    }

    //endregion
}