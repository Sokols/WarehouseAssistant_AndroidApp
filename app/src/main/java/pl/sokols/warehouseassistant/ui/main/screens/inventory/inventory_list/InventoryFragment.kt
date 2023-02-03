package pl.sokols.warehouseassistant.ui.main.screens.inventory.inventory_list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.FragmentInventoryBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.adapters.InventoriesAdapter
import pl.sokols.warehouseassistant.utils.AlertUtils
import pl.sokols.warehouseassistant.utils.extensions.addSwipe
import pl.sokols.warehouseassistant.utils.extensions.setupDivider
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class InventoryFragment : BaseFragment() {

    private val viewModel: InventoryViewModel by viewModels()
    override val binding by viewBinding(FragmentInventoryBinding::bind)
    override fun getLayoutRes(): Int = R.layout.fragment_inventory
    private val inventoriesAdapter = InventoriesAdapter { onItemClick(it) }

    //region Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupButtons()
        setupObservers()
    }

    //endregion

    //region Setup observers

    private fun setupObservers() {
        viewModel.getInventories().observe(viewLifecycleOwner) {
            inventoriesAdapter.submitList(it)
            setView(it)
        }
    }

    //endregion

    //region Setup UI Components

    private fun setupButtons() {
        binding.startInventoryButton.setOnClickListener { onStartInventoryClicked() }
    }

    private fun initRecyclerView() {
        binding.inventoriesRecyclerView.apply {
            adapter = inventoriesAdapter
            setupDivider()
            addSwipe { deleteItemAt(it) }
        }
    }

    //endregion

    //region Actions

    private fun onStartInventoryClicked() {
        val directions = InventoryFragmentDirections
            .actionInventoryFragmentToInventoryProcedureFragment(null)
        findNavController().navigate(directions)
    }

    //endregion

    //region Helpers

    private fun deleteItemAt(index: Int) {
        val deletedInventory = inventoriesAdapter.currentList[index] as Inventory
        viewModel.deleteInventory(deletedInventory)

        AlertUtils.showMessage(requireView(), R.string.deleted, R.string.undo) {
            viewModel.addInventory(deletedInventory)
        }
    }

    private fun setView(list: List<Inventory>?) {
        binding.apply {
            loading.isVisible = false
            val emptyVisibility = list.isNullOrEmpty()
            emptyLayout.emptyLayout.isVisible = emptyVisibility
            inventoriesRecyclerView.isVisible = !emptyVisibility
        }
    }

    private fun onItemClick(inventory: Inventory) {
        val directions =
            InventoryFragmentDirections.actionInventoryFragmentToSummaryFragment(inventory)
        findNavController().navigate(directions)
    }
}