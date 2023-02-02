package pl.sokols.warehouseassistant.ui.main.screens.inventory.procedure

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import pl.sokols.warehouseassistant.utils.extensions.setupDivider

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

    //region Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
        setupObservers()
        inventory = args.inventory
        viewModel.setItems(inventory)
    }

    //endregion

    //region Setup observers

    private fun setupObservers() {
        viewModel.apply {
            tempItems.observe(viewLifecycleOwner) {
                allItems = it
            }
            items.observe(viewLifecycleOwner) {
                itemsAdapter.submitList(it)
                setView(it)
            }
        }
    }

    //endregion

    //region NFC setup

    fun retrieveIntent(intent: Intent?) {
        val nfcData = viewModel.retrieveNFC(intent)
        if (nfcData is NfcState) {
            NFCUtil.displayToast(context, nfcData)
        } else if (nfcData is CountedItem) {
            val item = nfcData.copy(id = nfcData.id)
            prepareItem(item, isEditing = false)
        }
    }

    //endregion

    //region Setup UI components

    private fun setComponents() {
        initRecyclerView()
        setButtonClickListeners()
        setupEmptyLayout()
    }

    private fun setupEmptyLayout() {
        binding.emptyLayout.apply {
            ivEmptyIcon.setImageResource(R.drawable.ic_baseline_nfc_24)
            tvEmptyDescription.setText(R.string.use_nfc_tag)
        }
    }

    private fun initRecyclerView() {
        binding.itemsRecyclerView.apply {
            itemsAdapter = ProcedureItemListAdapter(mainListener)
            adapter = itemsAdapter
            setupDivider()
            addSwipeToDelete()
        }
    }

    private fun setButtonClickListeners() {
        binding.apply {
            applyDialogButton.setOnClickListener {
                val item = item
                if (item != null) {
                    viewModel.addEditItem(item, isEditing, selectedItemIndex)
                    resetItems()
                }
            }

            finishFAB.setOnClickListener { displayConfirmationDialog() }
            addItemFAB.setOnClickListener { displayAddItemDialog() }
            searchFAB.setOnClickListener { displaySearchItemDialog() }
        }
    }

    //endregion

    //region Navigation

    private fun navigateToInventory() {
        val inventory = viewModel.prepareInventory(inventory)
        inventory?.let {
            val directions = InventoryProcedureFragmentDirections
                .actionInventoryProcedureFragmentToSummaryFragment(it)
            findNavController().navigate(directions)
        }
    }

    //endregion

    //region Helpers

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
        binding.apply {
            loading.isVisible = false
            val emptyVisibility = list.isNullOrEmpty()
            emptyLayout.emptyLayout.isVisible = emptyVisibility
            itemsRecyclerView.isVisible = !emptyVisibility
        }
    }

    private fun resetItems() {
        selectedItemIndex?.let { itemsAdapter.notifyItemChanged(it) }
        binding.item = null
        itemsAdapter.resetPosition()
    }

    private fun prepareItem(item: CountedItem, isEditing: Boolean, index: Int? = null) {
        binding.apply {
            this.item = item
            this@InventoryProcedureFragment.isEditing = isEditing
            this@InventoryProcedureFragment.selectedItemIndex = index
            applyDialogButton.text =
                if (isEditing) getString(R.string.correct) else getString(R.string.confirm)
        }
    }

    private val mainListener = object : (Int, Any) -> Unit {
        override fun invoke(index: Int, item: Any) {
            prepareItem(item as CountedItem, isEditing = true, index)
        }
    }

    private fun displayConfirmationDialog() {
        Utils.displayLogoutDialog(
            requireContext(),
            getString(R.string.are_you_sure_to_finish_inventory)
        ) { _, _ ->
            navigateToInventory()
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

    //endregion
}