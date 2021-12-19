package pl.sokols.warehouseassistant.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.ProcedureItemBinding
import pl.sokols.warehouseassistant.utils.ItemDiffCallback

class ProcedureItemListAdapter(
    private val mainListener: (Int, Any) -> Unit
) : ListAdapter<Any, ProcedureItemListAdapter.ProcedureItemListViewHolder>(ItemDiffCallback) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class ProcedureItemListViewHolder(
        private val binding: ProcedureItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: CountedItem) {
            binding.item = item.copy(id = item.id)
            itemView.setOnClickListener {
                val tempItem = binding.item!!
                mainListener(layoutPosition, tempItem)
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
    ): ProcedureItemListViewHolder = ProcedureItemListViewHolder(
        ProcedureItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ProcedureItemListViewHolder, position: Int) =
        holder.bind(getItem(position) as CountedItem)

    fun resetPosition() {
        selectedPosition = RecyclerView.NO_POSITION
    }
}