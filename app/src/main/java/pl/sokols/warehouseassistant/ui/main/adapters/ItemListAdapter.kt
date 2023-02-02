package pl.sokols.warehouseassistant.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.ItemBinding

class ItemListAdapter(
    private val onItemClick: (CountedItem) -> Unit,
    private val onNfcTagClick: (CountedItem) -> Unit
) : ListAdapter<CountedItem, ItemListAdapter.ItemListViewHolder>(BasicItemListAdapter.ItemDiffCallback) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class ItemListViewHolder(
        private val binding: ItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: CountedItem) {
            binding.item = item
            itemView.setOnClickListener {
                onItemClick(item)
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
            }

            itemView.isSelected = selectedPosition == layoutPosition

            binding.nfcTagButton.setOnClickListener {
                onNfcTagClick(item)
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
        holder.bind(getItem(position))
    }

    fun getItemPosition(nfcData: CountedItem): Int = currentList.indexOf(nfcData)
}