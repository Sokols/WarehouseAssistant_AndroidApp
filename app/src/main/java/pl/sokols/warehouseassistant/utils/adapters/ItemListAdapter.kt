package pl.sokols.warehouseassistant.utils.adapters

import android.annotation.SuppressLint
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
    private val nfcListener: OnItemClickListener? = null
) : ListAdapter<Any, ItemListAdapter.ItemListViewHolder>(ItemDiffCallback) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION
    private var previousPosition: Int = RecyclerView.NO_POSITION

    inner class ItemListViewHolder(
        private val binding: ItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(
            item: Item,
            mainListener: OnItemClickListener,
            nfcListener: OnItemClickListener?,
            position: Int
        ) {
            binding.item = item
            itemView.setOnClickListener {
                mainListener.onItemClickListener(item)
                previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
            }

            itemView.isSelected = position == selectedPosition

            binding.nfcTagButton.setOnClickListener {
                nfcListener?.onItemClickListener(item)
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
        holder.bind(getItem(position) as Item, mainListener, nfcListener, position)
    }

    fun getItemPosition(nfcData: Item): Int = currentList.indexOf(nfcData)
}