package pl.sokols.warehouseassistant.ui.main.screens.inventory.summary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.FragmentSummaryBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.adapters.CountedItemAdapterType
import pl.sokols.warehouseassistant.ui.main.adapters.CountedItemsAdapter
import pl.sokols.warehouseassistant.utils.extensions.setupDivider
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class SummaryFragment : BaseFragment() {

    private val viewModel: SummaryViewModel by viewModels()
    override val binding by viewBinding(FragmentSummaryBinding::bind)
    override fun getLayoutRes(): Int = R.layout.fragment_summary
    private val args: SummaryFragmentArgs by navArgs()

    private val itemsAdapter = CountedItemsAdapter(CountedItemAdapterType.SUMMARY)

    //region Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.inventory = args.inventory
        binding.inventory = viewModel.inventory
        setComponents()
    }

    //endregion

    //region Setup UI components

    private fun setComponents() {
        initRecyclerView()
        setButtonClickListeners()
    }

    private fun initRecyclerView() {
        binding.itemsRecyclerView.apply {
            adapter = itemsAdapter
            val list = viewModel.inventory.items.values.toMutableList()
            itemsAdapter.submitList(list)
            setupDivider()
        }
    }

    private fun setButtonClickListeners() {
        binding.apply {
            editFAB.setOnClickListener { onEditClicked() }
            shareFAB.setOnClickListener { viewModel.shareInventory() }
        }
    }

    //endregion

    //region Actions

    private fun onEditClicked() {
        val directions =
            SummaryFragmentDirections.actionSummaryFragmentToInventoryProcedureFragment(viewModel.inventory)
        findNavController().navigate(directions)
    }

    //endregion
}