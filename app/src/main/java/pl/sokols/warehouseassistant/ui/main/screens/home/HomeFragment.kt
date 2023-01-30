package pl.sokols.warehouseassistant.ui.main.screens.home

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.HomeFragmentBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    override val binding: HomeFragmentBinding by viewBinding(HomeFragmentBinding::bind)
    override fun getLayoutRes(): Int = R.layout.home_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.user = FirebaseAuth.getInstance().currentUser
    }
}