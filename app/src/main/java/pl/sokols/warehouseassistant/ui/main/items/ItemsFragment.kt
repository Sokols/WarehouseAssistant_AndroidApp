package pl.sokols.warehouseassistant.ui.main.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.ItemsFragmentBinding
import pl.sokols.warehouseassistant.ui.main.items.adapters.ItemListAdapter
import pl.sokols.warehouseassistant.utils.DividerItemDecorator
import pl.sokols.warehouseassistant.utils.OnItemClickListener

@AndroidEntryPoint
class ItemsFragment : Fragment() {

    private val viewModel: ItemsViewModel by viewModels()
    private lateinit var binding: ItemsFragmentBinding
    private lateinit var recyclerViewAdapter: ItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemsFragmentBinding.inflate(inflater, container, false)
        setComponents()
        setListeners()
        return binding.root
    }

    private fun setComponents() {
        recyclerViewAdapter = ItemListAdapter()
        viewModel.getItems().observe(viewLifecycleOwner, { list ->
            recyclerViewAdapter.submitList(list)
        })
        binding.itemsRecyclerView.adapter = recyclerViewAdapter
        binding.itemsRecyclerView.addItemDecoration(
            DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider,
                    null
                )!!
            )
        )
    }

    private fun setListeners() {
        binding.addItemButton.setOnClickListener {
            addEditItem(null, object : OnItemClickListener {
                override fun onItemClickListener(item: Any) {
                    viewModel.addItem(item as Item)
                }
            })
        }
    }

    private fun addEditItem(item: Item?, listener: OnItemClickListener) {
        ItemAddEditDialog(item, listener).show(
            requireFragmentManager(),
            getString(R.string.provide_item_dialog)
        )
    }
}