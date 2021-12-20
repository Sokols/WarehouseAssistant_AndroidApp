package pl.sokols.warehouseassistant.ui.main.screens.inventory.inventory_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.InventoryFragmentBinding
import pl.sokols.warehouseassistant.ui.main.adapters.InventoryListAdapter
import pl.sokols.warehouseassistant.utils.DividerItemDecorator
import pl.sokols.warehouseassistant.utils.SwipeHelper

@AndroidEntryPoint
class InventoryFragment : Fragment() {

    private val viewModel: InventoryViewModel by viewModels()
    private lateinit var binding: InventoryFragmentBinding
    private lateinit var inventoriesAdapter: InventoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InventoryFragmentBinding.inflate(inflater, container, false)
        setComponents()
        return binding.root
    }

    private fun setComponents() {
        inventoriesAdapter = InventoryListAdapter(mainListener)
        viewModel.getInventories().observe(viewLifecycleOwner, {
            inventoriesAdapter.submitList(it)
            setView(it)
        })
        binding.inventoriesRecyclerView.adapter = inventoriesAdapter
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
            it.findNavController()
                .navigate(
                    InventoryFragmentDirections.actionInventoryFragmentToInventoryProcedureFragment(
                        null
                    )
                )
        }

        addSwipeToDelete()
    }

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