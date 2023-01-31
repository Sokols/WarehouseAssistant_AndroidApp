package pl.sokols.warehouseassistant.ui.main.screens.items

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.ItemsFragmentBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.adapters.ItemListAdapter
import pl.sokols.warehouseassistant.ui.main.dialogs.ItemAddEditDialog
import pl.sokols.warehouseassistant.ui.main.dialogs.WriteNfcDialog
import pl.sokols.warehouseassistant.utils.*

@AndroidEntryPoint
class ItemsFragment : BaseFragment() {

    private lateinit var nfcDialog: WriteNfcDialog
    private val viewModel: ItemsViewModel by viewModels()
    override val binding by viewBinding(ItemsFragmentBinding::bind)
    override fun getLayoutRes(): Int = R.layout.items_fragment
    private lateinit var recyclerViewAdapter: ItemListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
        setListeners()
    }

    fun retrieveIntent(intent: Intent?) {
        val nfcData = viewModel.retrieveNFC(intent)
        if (nfcData is NfcState) {
            NFCUtil.displayToast(context, nfcData)
        } else if (nfcData is CountedItem) {
            invokeItemClick(nfcData)
        }
        viewModel.changeNfcState()
    }

    private fun setComponents() {
        nfcDialog = WriteNfcDialog { viewModel.changeNfcState() }
        initRecyclerView()
    }

    private fun setListeners() {
        binding.addItemButton.setOnClickListener {
            addEditItem(null, object : (Any) -> Unit {
                override fun invoke(item: Any) {
                    viewModel.addItem(item as CountedItem)
                }
            })
        }
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = ItemListAdapter(mainListener, nfcListener)
        viewModel.getItems().observe(viewLifecycleOwner) {
            recyclerViewAdapter.submitList(it)
            setView(it)
        }
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

    private fun invokeItemClick(nfcData: CountedItem) {
        val position: Int = recyclerViewAdapter.getItemPosition(nfcData)
        binding.itemsRecyclerView.scrollToPosition(position)
        binding.itemsRecyclerView.postDelayed({
            binding.itemsRecyclerView.findViewHolderForLayoutPosition(position)?.itemView?.performClick()
        }, 50)
    }

    private fun setView(list: List<CountedItem>?) {
        binding.loading.isVisible = false
        val emptyVisibility = list.isNullOrEmpty()
        binding.emptyLayout.emptyLayout.isVisible = emptyVisibility
        binding.itemsRecyclerView.isVisible = !emptyVisibility
    }

    private fun addEditItem(item: CountedItem?, listener: (Any) -> Unit) {
        activity?.let {
            ItemAddEditDialog(item, listener).show(
                it.supportFragmentManager,
                getString(R.string.provide_item_dialog)
            )
        }
    }

    private fun addSwipeToDelete() {
        ItemTouchHelper(object : SwipeHelper(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem: CountedItem =
                    recyclerViewAdapter.currentList[viewHolder.layoutPosition] as CountedItem
                viewModel.deleteItem(deletedItem)

                Snackbar
                    .make(requireView(), getString(R.string.deleted), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.undo)) { viewModel.updateItem(deletedItem) }
                    .show()
            }
        }).attachToRecyclerView(binding.itemsRecyclerView)
    }

    private val mainListener = object : (Any) -> Unit {
        override fun invoke(item: Any) {
            addEditItem(item as CountedItem, object : (Any) -> Unit {
                @SuppressLint("NotifyDataSetChanged")
                override fun invoke(item: Any) {
                    viewModel.updateItem(item as CountedItem)
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            })
        }
    }

    private val nfcListener = object : (Any) -> Unit {
        override fun invoke(item: Any) {
            viewModel.changeNfcState(NfcState.WRITE, (item as CountedItem).id)
            activity?.let {
                nfcDialog.show(
                    it.supportFragmentManager,
                    getString(R.string.provide_nfc_dialog)
                )
            }
        }
    }
}