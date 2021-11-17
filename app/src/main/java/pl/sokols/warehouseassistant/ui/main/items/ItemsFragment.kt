package pl.sokols.warehouseassistant.ui.main.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.databinding.ItemsFragmentBinding

@AndroidEntryPoint
class ItemsFragment : Fragment() {

    private val viewModel: ItemsViewModel by viewModels()
    private lateinit var binding: ItemsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}