package pl.sokols.warehouseassistant.ui.main.inventory.procedure

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.InventoryProcedureFragmentBinding
import pl.sokols.warehouseassistant.utils.DividerItemDecorator
import pl.sokols.warehouseassistant.utils.NFCUtil
import pl.sokols.warehouseassistant.utils.NfcState
import pl.sokols.warehouseassistant.utils.OnItemClickListener
import pl.sokols.warehouseassistant.utils.adapters.ItemListAdapter

@AndroidEntryPoint
class InventoryProcedureFragment : Fragment() {

    private val viewModel: InventoryProcedureViewModel by viewModels()
    private lateinit var binding: InventoryProcedureFragmentBinding
    private lateinit var recyclerViewAdapter: ItemListAdapter

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
        val position: Int = recyclerViewAdapter.getItemPosition(nfcData)
        binding.itemsRecyclerView.scrollToPosition(position)
        binding.itemsRecyclerView.postDelayed({
            binding.itemsRecyclerView.findViewHolderForLayoutPosition(position)?.itemView?.performClick()
        }, 50)
    }

    private fun setComponents() {
        recyclerViewAdapter = ItemListAdapter(mainListener)
        viewModel.getItems().observe(viewLifecycleOwner, {
            recyclerViewAdapter.submitList(it)
        })
        binding.itemsRecyclerView.adapter = recyclerViewAdapter
        binding.itemsRecyclerView.addItemDecoration(
            DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider,
                    null
                )!!
            )
        )
    }

    private val mainListener = object : OnItemClickListener {
        override fun onItemClickListener(item: Any) {
            binding.item = item as Item
        }
    }
}