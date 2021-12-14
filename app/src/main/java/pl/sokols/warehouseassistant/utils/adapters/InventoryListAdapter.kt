package pl.sokols.warehouseassistant.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.InventoryItemBinding
import pl.sokols.warehouseassistant.utils.ItemDiffCallback
import pl.sokols.warehouseassistant.utils.OnItemClickListener

class InventoryListAdapter(
    private val mainListener: OnItemClickListener
) : ListAdapter<Any, InventoryListAdapter.InventoryListViewHolder>(ItemDiffCallback) {

    inner class InventoryListViewHolder(
        private val binding: InventoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(inventory: Inventory, mainListener: OnItemClickListener) {
            binding.inventory = inventory
            binding.itemLayout.setOnClickListener {
                mainListener.onItemClickListener(inventory)
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
        holder.bind(getItem(position) as Inventory, mainListener)
    }
}