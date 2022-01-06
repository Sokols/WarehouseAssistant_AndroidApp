package pl.sokols.warehouseassistant.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.SummaryItemBinding
import pl.sokols.warehouseassistant.utils.ItemDiffCallback

class SummaryListAdapter :
    ListAdapter<Any, SummaryListAdapter.SummaryListViewHolder>(ItemDiffCallback) {
    inner class SummaryListViewHolder(
        private val binding: SummaryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CountedItem) {
            binding.item = item.copy(id = item.id)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SummaryListViewHolder = SummaryListViewHolder(
        SummaryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SummaryListViewHolder, position: Int) =
        holder.bind(getItem(position) as CountedItem)
}