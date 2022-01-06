package pl.sokols.warehouseassistant.ui.main.screens.inventory.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.SummaryFragmentBinding
import pl.sokols.warehouseassistant.ui.main.adapters.SummaryListAdapter
import pl.sokols.warehouseassistant.utils.DividerItemDecorator

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private val viewModel: SummaryViewModel by viewModels()
    private lateinit var binding: SummaryFragmentBinding
    private val args: SummaryFragmentArgs by navArgs()
    private lateinit var inventory: Inventory
    private lateinit var itemsAdapter: SummaryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SummaryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inventory = args.inventory
        binding.inventory = inventory
        setComponents()
    }

    private fun setComponents() {
        initRecyclerView()
        setButtonClickListeners()
    }

    private fun initRecyclerView() {
        itemsAdapter = SummaryListAdapter()
        binding.itemsRecyclerView.adapter = itemsAdapter
        itemsAdapter.submitList(ArrayList(inventory.items.values).toMutableList() as List<Any>?)

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

    private fun setButtonClickListeners() {
        binding.editFAB.setOnClickListener {
            it.findNavController().navigate(
                SummaryFragmentDirections
                    .actionSummaryFragmentToInventoryProcedureFragment(binding.inventory)
            )
        }

        binding.shareFAB.setOnClickListener {
            viewModel.shareInventory(inventory)
        }
    }
}