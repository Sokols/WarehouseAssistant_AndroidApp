package pl.sokols.warehouseassistant.ui.main.screens.inventory.inventory_list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.InventoryFragmentBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.adapters.InventoryListAdapter
import pl.sokols.warehouseassistant.utils.SwipeHelper
import pl.sokols.warehouseassistant.utils.extensions.setupDivider
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class InventoryFragment : BaseFragment() {

    private val viewModel: InventoryViewModel by viewModels()
    override val binding by viewBinding(InventoryFragmentBinding::bind)
    override fun getLayoutRes(): Int = R.layout.inventory_fragment
    private lateinit var inventoriesAdapter: InventoryListAdapter

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
            inventoriesAdapter = InventoryListAdapter(mainListener)
            adapter = inventoriesAdapter
            setupDivider()
            addSwipeToDelete()
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

    private fun addSwipeToDelete() {
        ItemTouchHelper(object : SwipeHelper(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedInventory: Inventory =
                    inventoriesAdapter.currentList[viewHolder.layoutPosition] as Inventory
                viewModel.deleteInventory(deletedInventory)

                Snackbar
                    .make(requireView(), getString(R.string.deleted), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.undo)) {
                        viewModel.addInventory(deletedInventory)
                    }
                    .show()
            }
        }).attachToRecyclerView(binding.inventoriesRecyclerView)
    }

    private fun setView(list: List<Inventory>?) {
        binding.apply {
            loading.isVisible = false
            val emptyVisibility = list.isNullOrEmpty()
            emptyLayout.emptyLayout.isVisible = emptyVisibility
            inventoriesRecyclerView.isVisible = !emptyVisibility
        }
    }

    private val mainListener = object : (Any) -> Unit {
        override fun invoke(inventory: Any) {
            val directions = InventoryFragmentDirections
                .actionInventoryFragmentToSummaryFragment(inventory as Inventory)
            findNavController().navigate(directions)
        }
    }
}