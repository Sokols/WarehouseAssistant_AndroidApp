package pl.sokols.warehouseassistant.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.ItemBinding
import pl.sokols.warehouseassistant.utils.ItemDiffCallback

class ItemListAdapter(
    private val mainListener: (Any) -> Unit,
    private val nfcListener: (Any) -> Unit = {}
) : ListAdapter<Any, ItemListAdapter.ItemListViewHolder>(ItemDiffCallback) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class ItemListViewHolder(
        private val binding: ItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(
            item: Item,
            mainListener: (Any) -> Unit,
            nfcListener: (Any) -> Unit
        ) {
            binding.item = item
            itemView.setOnClickListener {
                mainListener(item)
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
            }

            itemView.isSelected = selectedPosition == layoutPosition

            binding.nfcTagButton.setOnClickListener {
                nfcListener(item)
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

    fun getItemPosition(nfcData: Item): Int = currentList.indexOf(nfcData)

    fun resetPosition() {
        selectedPosition = RecyclerView.NO_POSITION
    }
}