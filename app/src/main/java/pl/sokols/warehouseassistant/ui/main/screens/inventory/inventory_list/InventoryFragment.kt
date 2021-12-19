package pl.sokols.warehouseassistant.ui.main.screens.inventory.inventory_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.InventoryFragmentBinding
import pl.sokols.warehouseassistant.ui.main.adapters.InventoryListAdapter
import pl.sokols.warehouseassistant.utils.DividerItemDecorator

@AndroidEntryPoint
class InventoryFragment : Fragment() {

    private val viewModel: InventoryViewModel by viewModels()
    private lateinit var binding: InventoryFragmentBinding
    private lateinit var recyclerViewAdapter: InventoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InventoryFragmentBinding.inflate(inflater, container, false)
        setComponents()
        return binding.root
    }

    private fun setComponents() {
        recyclerViewAdapter = InventoryListAdapter(mainListener)
        viewModel.getInventories().observe(viewLifecycleOwner, {
            recyclerViewAdapter.submitList(it)
        })
        binding.inventoriesRecyclerView.adapter = recyclerViewAdapter
        binding.inventoriesRecyclerView.addItemDecoration(
            DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider,
                    null
                )!!
            )
        )

        binding.startInventoryButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_inventoryFragment_to_inventoryProcedureFragment)
        }
    }

    private val mainListener = object : (Any) -> Unit {
        override fun invoke(item: Any) {
            TODO("Not yet implemented")
        }
    }
}