package pl.sokols.warehouseassistant.ui.main.screens.items

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.ItemsFragmentBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.adapters.ItemListAdapter
import pl.sokols.warehouseassistant.ui.main.dialogs.ItemAddEditDialog
import pl.sokols.warehouseassistant.ui.main.dialogs.WriteNfcDialog
import pl.sokols.warehouseassistant.utils.*
import pl.sokols.warehouseassistant.utils.extensions.addSwipe
import pl.sokols.warehouseassistant.utils.extensions.setupDivider

@AndroidEntryPoint
class ItemsFragment : BaseFragment() {

    private lateinit var nfcDialog: WriteNfcDialog
    private val viewModel: ItemsViewModel by viewModels()
    override val binding by viewBinding(ItemsFragmentBinding::bind)
    override fun getLayoutRes(): Int = R.layout.items_fragment
    private val recyclerViewAdapter = ItemListAdapter({ onItemClick(it) }, { onNfcTagClick(it) })

    //region Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
        setupButtons()
        setupObservers()
    }

    //endregion

    //region Observers

    private fun setupObservers() {
        viewModel.getItems().observe(viewLifecycleOwner) {
            recyclerViewAdapter.submitList(it)
            setView(it)
        }
    }

    //endregion

    //region NFC setup

    fun retrieveIntent(intent: Intent?) {
        val nfcData = viewModel.retrieveNFC(intent)
        if (nfcData is NfcState) {
            NFCUtil.displayToast(context, nfcData)
        } else if (nfcData is CountedItem) {
            invokeItemClick(nfcData)
        }
        viewModel.changeNfcState()
    }

    private fun invokeItemClick(nfcData: CountedItem) {
        val position: Int = recyclerViewAdapter.getItemPosition(nfcData)
        binding.itemsRecyclerView.apply {
            scrollToPosition(position)
            postDelayed({
                findViewHolderForLayoutPosition(position)?.itemView?.performClick()
            }, 50)
        }
    }

    //endregion

    //region Setup UI elements

    private fun setComponents() {
        nfcDialog = WriteNfcDialog { viewModel.changeNfcState() }
        initRecyclerView()
    }

    private fun setupButtons() {
        binding.addItemButton.setOnClickListener { onAddItemClicked() }
    }

    private fun initRecyclerView() {
        binding.itemsRecyclerView.apply {
            adapter = recyclerViewAdapter
            setupDivider()
            addSwipe { deleteItemAt(it) }
        }
    }

    private fun setView(list: List<CountedItem>?) {
        binding.apply {
            loading.isVisible = false
            val emptyVisibility = list.isNullOrEmpty()
            emptyLayout.emptyLayout.isVisible = emptyVisibility
            itemsRecyclerView.isVisible = !emptyVisibility
        }
    }

    //endregion

    //region Actions

    private fun onAddItemClicked() {
        addEditItem(null) {
            viewModel.addItem(it)
        }
    }

    private fun addEditItem(item: CountedItem?, listener: (CountedItem) -> Unit) {
        activity?.let {
            ItemAddEditDialog(item, listener).show(
                it.supportFragmentManager,
                getString(R.string.provide_item_dialog)
            )
        }
    }

    //endregion

    //region Helpers

    private fun deleteItemAt(index: Int) {
        val deletedItem = recyclerViewAdapter.currentList[index] as CountedItem
        viewModel.deleteItem(deletedItem)

        AlertUtils.showMessage(requireView(), R.string.deleted, R.string.undo) {
            viewModel.updateItem(deletedItem)
        }
    }

    private fun onItemClick(item: CountedItem) {
        addEditItem(item) {
            viewModel.updateItem(item)
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun onNfcTagClick(item: CountedItem) {
        viewModel.changeNfcState(NfcState.WRITE, item.id)
        activity?.let {
            nfcDialog.show(
                it.supportFragmentManager,
                getString(R.string.provide_nfc_dialog)
            )
        }
    }

    //endregion
}