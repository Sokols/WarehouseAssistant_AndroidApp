package pl.sokols.warehouseassistant.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.SummaryItemBinding

class SummaryListAdapter :
    ListAdapter<CountedItem, SummaryListAdapter.SummaryListViewHolder>(BasicItemListAdapter.ItemDiffCallback) {

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
    ): SummaryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SummaryItemBinding.inflate(inflater, parent, false)
        return SummaryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}