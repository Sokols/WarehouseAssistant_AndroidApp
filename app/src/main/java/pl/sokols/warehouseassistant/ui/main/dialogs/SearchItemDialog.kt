package pl.sokols.warehouseassistant.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.SearchItemDialogBinding
import pl.sokols.warehouseassistant.ui.main.adapters.BasicItemListAdapter
import pl.sokols.warehouseassistant.utils.DividerItemDecorator

class SearchItemDialog(
    private val listener: (CountedItem) -> Unit,
    private val items: List<CountedItem>
) : DialogFragment() {

    private lateinit var binding: SearchItemDialogBinding
    private var matchedItems: ArrayList<CountedItem> = arrayListOf()
    private lateinit var itemsAdapter: BasicItemListAdapter

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchItemDialogBinding.inflate(inflater, container, false)
        setComponents()
        return binding.root
    }

    private fun setComponents() {
        initRecyclerView()
        setSearching()

        binding.applyDialogButton.setOnClickListener {
            val item: CountedItem? = binding.item
            item?.let {
                listener(item)
            }
            dismiss()
        }
    }

    private fun initRecyclerView() {
        itemsAdapter = BasicItemListAdapter(searchItemListener).also {
            binding.recyclerView.adapter = it
            it.submitList(items)
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider,
                    null
                )!!
            )
        )
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
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        binding.recyclerView.apply {
            itemsAdapter.submitList(matchedItems as List<Any>?)
        }
    }

    private val searchItemListener = object : (Any) -> Unit {
        override fun invoke(item: Any) {
            binding.item = item as CountedItem
            binding.applyDialogButton.isEnabled = true
        }
    }
}
