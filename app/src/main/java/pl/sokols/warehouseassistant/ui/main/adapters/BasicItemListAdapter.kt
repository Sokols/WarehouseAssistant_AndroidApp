package pl.sokols.warehouseassistant.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.BasicItemBinding
import pl.sokols.warehouseassistant.databinding.ProcedureItemBinding
import pl.sokols.warehouseassistant.utils.ItemDiffCallback

class BasicItemListAdapter(
    private val mainListener: (Any) -> Unit
) : ListAdapter<Any, BasicItemListAdapter.BasicItemListViewHolder>(ItemDiffCallback) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class BasicItemListViewHolder(
        private val binding: BasicItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: CountedItem) {
            binding.item = item.copy(id = item.id)
            itemView.setOnClickListener {
                val tempItem = binding.item!!
                mainListener(tempItem)
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
            }

            itemView.isSelected = selectedPosition == layoutPosition
        }
    }

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

    override fun onBindViewHolder(holder: BasicItemListViewHolder, position: Int) =
        holder.bind(getItem(position) as CountedItem)
}