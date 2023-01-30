package pl.sokols.warehouseassistant.ui.main.screens.inventory.procedure

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.InventoryProcedureFragmentBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.adapters.ProcedureItemListAdapter
import pl.sokols.warehouseassistant.ui.main.dialogs.ItemAddEditDialog
import pl.sokols.warehouseassistant.ui.main.dialogs.SearchItemDialog
import pl.sokols.warehouseassistant.utils.*

@AndroidEntryPoint
class InventoryProcedureFragment : BaseFragment() {

    private val viewModel: InventoryProcedureViewModel by viewModels()
    override val binding by viewBinding(InventoryProcedureFragmentBinding::bind)
    override fun getLayoutRes(): Int = R.layout.inventory_procedure_fragment
    private lateinit var itemsAdapter: ProcedureItemListAdapter
    private val args: InventoryProcedureFragmentArgs by navArgs()

    private var inventory: Inventory? = null
    private var isEditing: Boolean = false
    private var selectedItemIndex: Int? = null
    private lateinit var allItems: List<CountedItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
        inventory = args.inventory
        viewModel.setItems(inventory)
    }

    fun retrieveIntent(intent: Intent?) {
        val nfcData = viewModel.retrieveNFC(intent)
        if (nfcData is NfcState) {
            NFCUtil.displayToast(context, nfcData)
        } else if (nfcData is CountedItem) {
            val item = nfcData.copy(id = nfcData.id)
            prepareItem(item, isEditing = false)
        }
    }

    private fun setComponents() {
        initRecyclerView()
        setButtonClickListeners()
        viewModel.tempItems.observe(viewLifecycleOwner, {
            allItems = it
        })
    }

    private fun initRecyclerView() {
        itemsAdapter = ProcedureItemListAdapter(mainListener)
        binding.itemsRecyclerView.adapter = itemsAdapter
        viewModel.items.observe(viewLifecycleOwner, {
            itemsAdapter.submitList(it)
            setView(it)
        })

        binding.itemsRecyclerView.addItemDecoration(
            DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider,
                    null
                )!!
            )
        )
        addSwipeToDelete()
    }

    private fun setButtonClickListeners() {
        binding.applyDialogButton.setOnClickListener {
            val item = binding.item
            if (item != null) {
                viewModel.addEditItem(item, isEditing, selectedItemIndex)
                resetItems()
            }
        }

        binding.finishFAB.setOnClickListener {
            displayConfirmationDialog(it)
        }

        binding.addItemFAB.setOnClickListener {
            displayAddItemDialog()
        }

        binding.searchFAB.setOnClickListener {
            displaySearchItemDialog()
        }
    }

    private fun addSwipeToDelete() {
        ItemTouchHelper(object : SwipeHelper(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val index = viewHolder.layoutPosition
                val deletedItem: CountedItem =
                    itemsAdapter.currentList[index] as CountedItem
                viewModel.deleteItem(index)

                Snackbar
                    .make(requireView(), getString(R.string.deleted), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.undo)) {
                        viewModel.addEditItem(deletedItem, isEditing = false)
                    }
                    .show()
            }
        }).attachToRecyclerView(binding.itemsRecyclerView)
    }

    private fun setView(list: List<CountedItem>?) {
        binding.loading.isVisible = false
        val emptyVisibility = list.isNullOrEmpty()
        binding.emptyLayout.emptyLayout.isVisible = emptyVisibility
        binding.itemsRecyclerView.isVisible = !emptyVisibility
    }

    private fun resetItems() {
        selectedItemIndex?.let { itemsAdapter.notifyItemChanged(it) }
        binding.item = null
        itemsAdapter.resetPosition()
    }

    private fun prepareItem(item: CountedItem, isEditing: Boolean, index: Int? = null) {
        binding.item = item
        this.isEditing = isEditing
        this.selectedItemIndex = index
        binding.applyDialogButton.text =
            if (isEditing) getString(R.string.correct) else getString(R.string.confirm)
    }

    private val mainListener = object : (Int, Any) -> Unit {
        override fun invoke(index: Int, item: Any) {
            prepareItem(item as CountedItem, isEditing = true, index)
        }
    }

    private fun displayConfirmationDialog(view: View) {
        Utils.displayLogoutDialog(
            requireContext(),
            getString(R.string.are_you_sure_to_finish_inventory)
        ) { _, _ ->
            val inventory = viewModel.prepareInventory(inventory)
            inventory?.let {
                view.findNavController()
                    .navigate(
                        InventoryProcedureFragmentDirections
                            .actionInventoryProcedureFragmentToSummaryFragment(inventory)
                    )
            }
        }.show()
    }

    private fun displayAddItemDialog() {
        activity?.let {
            ItemAddEditDialog(null, object : (Any) -> Unit {
                override fun invoke(item: Any) {
                    viewModel.addNewItem(item as CountedItem)
                }
            }).show(
                it.supportFragmentManager,
                getString(R.string.provide_item_dialog)
            )
        }
    }

    private fun displaySearchItemDialog() {
        activity?.let {
            SearchItemDialog(searchItemListener, allItems).show(
                it.supportFragmentManager,
                getString(R.string.provide_item_dialog)
            )
        }
    }

    private val searchItemListener = object : (CountedItem) -> Unit {
        override fun invoke(item: CountedItem) {
            prepareItem(item, isEditing = false)
        }
    }
}