package pl.sokols.warehouseassistant.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.SearchItemDialogBinding
import pl.sokols.warehouseassistant.ui.main.adapters.BasicItemListAdapter
import pl.sokols.warehouseassistant.utils.extensions.setupDivider

class SearchItemDialog(
    private val listener: (CountedItem) -> Unit,
    private val items: List<CountedItem>
) : DialogFragment() {

    private lateinit var binding: SearchItemDialogBinding
    private var matchedItems: ArrayList<CountedItem> = arrayListOf()
    private lateinit var itemsAdapter: BasicItemListAdapter

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchItemDialogBinding.inflate(inflater, container, false)
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
            itemsAdapter = BasicItemListAdapter(searchItemListener)
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
            listener(it)
        }
        dismiss()
    }

    //endregion

    //region Helpers

    private fun search(text: String?) {
        matchedItems = arrayListOf()

        text?.let {
            items.forEach { item ->
                if (item.name.contains(text, true) ||
                    item.id.contains(text, true)
                ) {
                    matchedItems.add(item)
                }
            }

            itemsAdapter.submitList(matchedItems as List<Any>?)
        }
    }

    private val searchItemListener = object : (Any) -> Unit {
        override fun invoke(item: Any) {
            binding.apply {
                this.item = item as CountedItem
                applyDialogButton.isEnabled = true
            }
        }
    }

    //endregion
}
