package pl.sokols.warehouseassistant.ui.main.items

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.ItemsFragmentBinding
import pl.sokols.warehouseassistant.utils.*
import pl.sokols.warehouseassistant.utils.adapters.ItemListAdapter
import pl.sokols.warehouseassistant.utils.dialogs.ItemAddEditDialog
import pl.sokols.warehouseassistant.utils.dialogs.WriteNfcDialog

@AndroidEntryPoint
class ItemsFragment : Fragment() {

    private lateinit var nfcDialog: WriteNfcDialog
    private val viewModel: ItemsViewModel by viewModels()
    private lateinit var binding: ItemsFragmentBinding
    private lateinit var recyclerViewAdapter: ItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemsFragmentBinding.inflate(inflater, container, false)
        setComponents()
        setListeners()
        return binding.root
    }

    fun retrieveIntent(intent: Intent?) {
        val nfcData = viewModel.retrieveNFC(intent)
        if (nfcData is NfcState) {
            NFCUtil.displayToast(context, nfcData)
        } else if (nfcData is Item) {
            invokeItemClick(nfcData)
        }
        viewModel.changeNfcState()
    }

    private fun invokeItemClick(nfcData: Item) {
        val position: Int = recyclerViewAdapter.getItemPosition(nfcData)
        binding.itemsRecyclerView.scrollToPosition(position)
        binding.itemsRecyclerView.postDelayed({
            binding.itemsRecyclerView.findViewHolderForLayoutPosition(position)?.itemView?.performClick()
        }, 50)
    }

    private fun setComponents() {
        nfcDialog = WriteNfcDialog { viewModel.changeNfcState() }
        recyclerViewAdapter = ItemListAdapter(mainListener, nfcListener)
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
        addSwipeToDelete()
    }

    private fun setListeners() {
        binding.addItemButton.setOnClickListener {
            addEditItem(null, object : OnItemClickListener {
                override fun onItemClickListener(item: Any) {
                    viewModel.addItem(item as Item)
                }
            })
        }
    }

    private fun addEditItem(item: Item?, listener: OnItemClickListener) {
        ItemAddEditDialog(item, listener).show(
            requireFragmentManager(),
            getString(R.string.provide_item_dialog)
        )
    }

    private fun addSwipeToDelete() {
        ItemTouchHelper(object : SwipeHelper(ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem: Item = recyclerViewAdapter.currentList[viewHolder.adapterPosition] as Item
                viewModel.deleteItem(deletedItem)

                Snackbar
                    .make(requireView(), getString(R.string.deleted), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.undo)) { viewModel.updateItem(deletedItem) }
                    .show()
            }
        }).attachToRecyclerView(binding.itemsRecyclerView)
    }

    private val mainListener = object : OnItemClickListener {
        override fun onItemClickListener(item: Any) {
            addEditItem(item as Item, object : OnItemClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClickListener(item: Any) {
                    viewModel.updateItem(item as Item)
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            })
        }
    }

    private val nfcListener = object : OnItemClickListener {
        override fun onItemClickListener(item: Any) {
            viewModel.changeNfcState(NfcState.WRITE, (item as Item).id)
            nfcDialog.show(
                requireFragmentManager(),
                getString(R.string.provide_nfc_dialog)
            )
        }
    }
}