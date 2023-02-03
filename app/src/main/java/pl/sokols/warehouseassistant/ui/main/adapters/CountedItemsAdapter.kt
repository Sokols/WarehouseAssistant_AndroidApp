package pl.sokols.warehouseassistant.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.ItemCountedItemBinding

enum class CountedItemAdapterType {
    SEARCH,
    ITEM,
    PROCEDURE,
    SUMMARY
}

class CountedItemsAdapter(
    private val adapterType: CountedItemAdapterType,
    private val onItemClick: ((position: Int, item: CountedItem) -> Unit)? = null,
    private val onNfcTagClick: ((CountedItem) -> Unit)? = null
) : ListAdapter<CountedItem, CountedItemsAdapter.ViewHolder>(ItemDiffCallback) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    //region Overridden

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountedItemBinding.inflate(inflater, parent, false)
        return when (adapterType) {
            CountedItemAdapterType.SEARCH -> SearchViewHolder(binding)
            CountedItemAdapterType.ITEM -> ItemViewHolder(binding)
            CountedItemAdapterType.PROCEDURE -> ProcedureViewHolder(binding)
            CountedItemAdapterType.SUMMARY -> SummaryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //endregion

    //region ViewHolders

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: CountedItem)
    }

    inner class SearchViewHolder(
        private val binding: ItemCountedItemBinding
    ) : ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        override fun bind(item: CountedItem) {
            binding.item = item.copy(id = item.id)
            itemView.setOnClickListener {
                val tempItem = binding.item!!
                onItemClick?.invoke(layoutPosition, tempItem)
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
            }

            itemView.isSelected = selectedPosition == layoutPosition
        }
    }

    inner class ItemViewHolder(
        private val binding: ItemCountedItemBinding
    ) : ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        override fun bind(item: CountedItem) {
            binding.item = item
            itemView.setOnClickListener {
                onItemClick?.invoke(layoutPosition, item)
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
            }

            itemView.isSelected = selectedPosition == layoutPosition

            binding.btnNfcTag.apply {
                setOnClickListener { onNfcTagClick?.invoke(item) }
                visibility = View.VISIBLE
            }
        }
    }

    inner class ProcedureViewHolder(
        private val binding: ItemCountedItemBinding
    ) : ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        override fun bind(item: CountedItem) {
            binding.apply {
                this.item = item.copy(id = item.id)
                tvAmount.visibility = View.VISIBLE
            }
            itemView.setOnClickListener {
                val tempItem = binding.item!!
                onItemClick?.invoke(layoutPosition, tempItem)
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
            }

            itemView.isSelected = selectedPosition == layoutPosition
        }
    }

    inner class SummaryViewHolder(
        private val binding: ItemCountedItemBinding
    ) : ViewHolder(binding.root) {

        override fun bind(item: CountedItem) {
            binding.apply {
                this.item = item.copy(id = item.id)
                tvAmount.visibility = View.VISIBLE
                tvDifference.visibility = View.VISIBLE
            }
        }
    }

    //endregion

    //region Public methods

    fun getItemPosition(nfcData: CountedItem): Int = currentList.indexOf(nfcData)

    fun resetPosition() {
        selectedPosition = RecyclerView.NO_POSITION
    }

    //endregion

    //region DiffCallback

    object ItemDiffCallback : DiffUtil.ItemCallback<CountedItem>() {
        override fun areItemsTheSame(oldItem: CountedItem, newItem: CountedItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CountedItem, newItem: CountedItem): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.name == newItem.name
                    && oldItem.amount == newItem.amount
                    && oldItem.difference == newItem.difference
                    && oldItem.price == newItem.price
        }
    }

    //endregion
}