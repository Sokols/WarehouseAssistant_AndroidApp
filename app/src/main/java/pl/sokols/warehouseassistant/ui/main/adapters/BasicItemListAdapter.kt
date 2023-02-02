package pl.sokols.warehouseassistant.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.BasicItemBinding

class BasicItemListAdapter(
    private val onItemClick: (CountedItem) -> Unit
) : ListAdapter<CountedItem, BasicItemListAdapter.BasicItemListViewHolder>(ItemDiffCallback) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    //region ViewHolder

    inner class BasicItemListViewHolder(
        private val binding: BasicItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: CountedItem) {
            binding.item = item.copy(id = item.id)
            itemView.setOnClickListener {
                val tempItem = binding.item!!
                onItemClick(tempItem)
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
            }

            itemView.isSelected = selectedPosition == layoutPosition
        }
    }

    //endregion

    //region Overridden

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasicItemListViewHolder = BasicItemListViewHolder(
        BasicItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: BasicItemListViewHolder, position: Int) {
        holder.bind(getItem(position))
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