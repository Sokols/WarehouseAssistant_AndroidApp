package pl.sokols.warehouseassistant.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.DialogSearchItemBinding
import pl.sokols.warehouseassistant.ui.main.adapters.CountedItemAdapterType
import pl.sokols.warehouseassistant.ui.main.adapters.CountedItemsAdapter
import pl.sokols.warehouseassistant.utils.extensions.setupDivider

class SearchItemDialog(
    private val items: List<CountedItem>,
    private val onApplyClick: (CountedItem) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogSearchItemBinding
    private val itemsAdapter = CountedItemsAdapter(
        CountedItemAdapterType.SEARCH,
        { _, item -> onItemClick(item) }
    )

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSearchItemBinding.inflate(inflater, container, false)
        setComponents()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    //endregion

    //region Setup UI components

    private fun setComponents() {
        initRecyclerView()
        setSearching()
        setupButtons()
    }

    private fun setupButtons() {
        binding.applyDialogButton.setOnClickListener { onApplyClicked() }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = itemsAdapter
            itemsAdapter.submitList(items)
            setupDivider()
        }
    }

    private fun setSearching() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                search(text)
                return true
            }
        })
    }

    //endregion

    //region Actions

    private fun onApplyClicked() {
        binding.item?.let {
            onApplyClick(it)
        }
        dismiss()
    }

    //endregion

    //region Helpers

    private fun search(text: String?) {
        text?.let { txt ->
            val matchedItems = items.filter { item ->
                item.name.contains(txt, true) || item.id.contains(txt, true)
            }
            itemsAdapter.submitList(matchedItems)
        }
    }

    private fun onItemClick(item: CountedItem) {
        binding.apply {
            this.item = item
            applyDialogButton.isEnabled = true
        }
    }

    //endregion
}
