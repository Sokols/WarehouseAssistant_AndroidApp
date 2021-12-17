package pl.sokols.warehouseassistant.ui.main.screens.inventory.procedure

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.InventoryProcedureFragmentBinding
import pl.sokols.warehouseassistant.ui.main.adapters.ProcedureItemListAdapter
import pl.sokols.warehouseassistant.utils.DividerItemDecorator
import pl.sokols.warehouseassistant.utils.NFCUtil
import pl.sokols.warehouseassistant.utils.NfcState

@AndroidEntryPoint
class InventoryProcedureFragment : Fragment() {

    private val viewModel: InventoryProcedureViewModel by viewModels()
    private lateinit var binding: InventoryProcedureFragmentBinding
    private lateinit var itemsAdapter: ProcedureItemListAdapter
    private lateinit var completedItemsAdapter: ProcedureItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        } else if (nfcData is Item) {
            invokeItemClick(nfcData)
        }
    }

    private fun invokeItemClick(nfcData: Item) {
        val position: Int = itemsAdapter.getItemPosition(nfcData)
        binding.itemsRecyclerView.scrollToPosition(position)
        binding.itemsRecyclerView.postDelayed({
            binding.itemsRecyclerView.findViewHolderForLayoutPosition(position)?.itemView?.performClick()
        }, 50)
    }

    private fun setComponents() {
        itemsAdapter = ProcedureItemListAdapter(mainListener)
        completedItemsAdapter = ProcedureItemListAdapter(mainListener)
        binding.itemsRecyclerView.adapter = itemsAdapter
        viewModel.tempItems.observe(viewLifecycleOwner, {
            viewModel.setItems(it)
        })
        viewModel.remainingItems.observe(viewLifecycleOwner, {
            itemsAdapter.submitList(it)
        })
        viewModel.completedItems.observe(viewLifecycleOwner, {
            completedItemsAdapter.submitList(it)
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
                viewModel.addItemToCompleted(item)
                resetItems()
            }
        }

        binding.finishInvetoryButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_inventoryProcedureFragment_to_summaryFragment)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                changeTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Unused method
            }

            // swap adapters related to selected tab
            private fun changeTab(tab: TabLayout.Tab?) {
                if (tab?.text == getString(R.string.itemsConfirmed)) {
                    resetItems(isCompleted = true)
                } else {
                    resetItems(isCompleted = false)
                }
            }
        })
    }

    private fun resetItems(isCompleted: Boolean = false) {
        if (isCompleted) {
            completedItemsAdapter.resetPosition()
            binding.itemsRecyclerView.adapter = completedItemsAdapter
        } else {
            itemsAdapter.resetPosition()
            binding.itemsRecyclerView.adapter = itemsAdapter
        }
        binding.item = null
    }

    private val mainListener = object : (Any) -> Unit {
        override fun invoke(item: Any) {
            binding.item = item as Item
        }
    }
}