package pl.sokols.warehouseassistant.ui.auth.registry

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.RegistryFragmentBinding
import pl.sokols.warehouseassistant.ui.auth.base.BaseAuthFragment
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class RegistryFragment : BaseAuthFragment() {

    override val viewModel: RegistryViewModel by viewModels()
    override val binding by viewBinding(RegistryFragmentBinding::bind)
    override fun getLayoutRes(): Int = R.layout.registry_fragment

    //region Overridden

    override fun setupViews() {
        etUsername = binding.etUsername
        etPassword = binding.etPassword
        loading = binding.loading
        btnMainAction = binding.btnRegister
        btnNavigation = binding.btnGoLogin
    }

    override fun onNavigationClicked() {
        val directions = RegistryFragmentDirections.actionRegistryFragmentToLoginFragment()
        findNavController().navigate(directions)
    }

    //endregion
}