package pl.sokols.warehouseassistant.ui.main.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.databinding.ItemsFragmentBinding
import pl.sokols.warehouseassistant.ui.main.items.adapters.ItemListAdapter

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
        recyclerViewAdapter = ItemListAdapter()
        binding.itemsRecyclerView.adapter = recyclerViewAdapter
        initObservers()
        return binding.root
    }

    private fun initObservers() {
        recyclerViewAdapter.submitList(viewModel.getItems())
    }
}