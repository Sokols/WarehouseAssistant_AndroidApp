package pl.sokols.warehouseassistant.ui.main.screens.inventory.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.databinding.SummaryFragmentBinding

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private val viewModel: SummaryViewModel by viewModels()
    private lateinit var binding: SummaryFragmentBinding
    private val args: SummaryFragmentArgs by navArgs()
    private lateinit var inventory: Inventory

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
        // TODO: Finish it.
    }
}