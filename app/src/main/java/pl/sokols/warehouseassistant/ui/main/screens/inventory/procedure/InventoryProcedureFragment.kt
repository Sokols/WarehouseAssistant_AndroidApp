package pl.sokols.warehouseassistant.ui.main.screens.inventory.procedure

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.InventoryProcedureFragmentBinding
import pl.sokols.warehouseassistant.ui.main.adapters.ProcedureItemListAdapter
import pl.sokols.warehouseassistant.utils.DividerItemDecorator
import pl.sokols.warehouseassistant.utils.NFCUtil
import pl.sokols.warehouseassistant.utils.NfcState
import pl.sokols.warehouseassistant.utils.Utils

@AndroidEntryPoint
class InventoryProcedureFragment : Fragment() {

    private val viewModel: InventoryProcedureViewModel by viewModels()
    private lateinit var binding: InventoryProcedureFragmentBinding
    private lateinit var itemsAdapter: ProcedureItemListAdapter

    private var isEditing: Boolean = false
    private var selectedItemIndex: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InventoryProcedureFragmentBinding.inflate(inflater, container, false)
        setComponents()
        return binding.root
    }

    fun retrieveIntent(intent: Intent?) {
        val nfcData = viewModel.retrieveNFC(intent)
        if (nfcData is NfcState) {
            NFCUtil.displayToast(context, nfcData)
        } else if (nfcData is CountedItem) {
            prepareItem(nfcData, isEditing = false)
        }
    }

    private fun setComponents() {
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

        binding.applyDialogButton.setOnClickListener {
            val item = binding.item
            if (item != null) {
                viewModel.addEditItem(item, isEditing, selectedItemIndex)
                resetItems()
            }
        }

        binding.finishInventoryButton.setOnClickListener {
            displayConfirmationDialog(it)
        }
    }

    private fun setView(list: List<CountedItem>?) {
        binding.loading.isVisible = false
        val emptyVisibility = list.isNullOrEmpty()
        binding.emptyLayout.emptyLayout.isVisible = emptyVisibility
        binding.itemsRecyclerView.isVisible = !emptyVisibility
    }

    @SuppressLint("NotifyDataSetChanged")
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
            view.findNavController()
                .navigate(
                    InventoryProcedureFragmentDirections.actionInventoryProcedureFragmentToSummaryFragment(
                        viewModel.prepareInventory()
                    )
                )
        }.show()
    }
}