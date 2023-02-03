package pl.sokols.warehouseassistant.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.ItemSummaryBinding

class InventoriesAdapter(
    private val onItemClick: (Inventory) -> Unit
) : ListAdapter<Inventory, InventoriesAdapter.InventoryListViewHolder>(ItemDiffCallback) {

    inner class InventoryListViewHolder(
        private val binding: ItemSummaryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(inventory: Inventory) {
            binding.apply {
                itemLayout.setOnClickListener { onItemClick(inventory) }
                ivIcon.setImageResource(R.drawable.ic_baseline_insert_drive_file_24)
                tvTitle.text = inventory.timestampCreated
                tvSubtitle.text = binding.root.context.getString(
                    R.string.timestamp_edited_and_value,
                    inventory.timestampEdited
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryListViewHolder = InventoryListViewHolder(
        ItemSummaryBinding.inflate(
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