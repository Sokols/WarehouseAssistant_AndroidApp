package pl.sokols.warehouseassistant.ui.main.screens.inventory.inventory_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.InventoryFragmentBinding
import pl.sokols.warehouseassistant.ui.auth.AuthActivity
import pl.sokols.warehouseassistant.ui.main.adapters.InventoryListAdapter
import pl.sokols.warehouseassistant.ui.main.screens.inventory.summary.SummaryFragmentDirections
import pl.sokols.warehouseassistant.utils.DividerItemDecorator
import pl.sokols.warehouseassistant.utils.Utils

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
            setView(it)
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

    private fun setView(list: List<Inventory>?) {
        binding.loading.isVisible = false
        val emptyVisibility = list.isNullOrEmpty()
        binding.emptyLayout.emptyLayout.isVisible = emptyVisibility
        binding.inventoriesRecyclerView.isVisible = !emptyVisibility
    }

    private val mainListener = object : (Any) -> Unit {
        override fun invoke(inventory: Any) {
            findNavController().navigate(
                InventoryFragmentDirections.actionInventoryFragmentToSummaryFragment(
                    inventory as Inventory
                )
            )
        }
    }
}