package pl.sokols.warehouseassistant.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.databinding.HomeFragmentBinding
import pl.sokols.warehouseassistant.ui.AuthActivity

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signOut.setOnClickListener {
            viewModel.logout()
            goToAuthActivity()
        }
    }

    private fun goToAuthActivity() {
        startActivity(Intent(activity, AuthActivity::class.java))
        activity?.finish()
    }
}