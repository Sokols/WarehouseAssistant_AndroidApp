package pl.sokols.warehouseassistant.ui.main.items.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.ItemBinding
import pl.sokols.warehouseassistant.utils.ItemDiffCallback
import pl.sokols.warehouseassistant.utils.OnItemClickListener

class ItemListAdapter(
    private val mainListener: OnItemClickListener,
    private val nfcListener: OnItemClickListener
) : ListAdapter<Any, ItemListAdapter.ItemListViewHolder>(ItemDiffCallback) {

    inner class ItemListViewHolder(
        private val binding: ItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, mainListener: OnItemClickListener, nfcListener: OnItemClickListener) {
            binding.item = item
            binding.itemLayout.setOnClickListener {
                mainListener.onItemClickListener(item)
            }

            binding.nfcTagButton.setOnClickListener {
                nfcListener.onItemClickListener(item)
            }
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
        holder.bind(getItem(position) as Item, mainListener, nfcListener)
    }
}