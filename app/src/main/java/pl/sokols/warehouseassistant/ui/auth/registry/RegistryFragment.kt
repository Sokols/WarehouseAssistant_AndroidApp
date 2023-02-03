package pl.sokols.warehouseassistant.ui.auth.registry

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.FragmentRegistryBinding
import pl.sokols.warehouseassistant.ui.auth.base.BaseAuthFragment
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class RegistryFragment : BaseAuthFragment() {

    override val viewModel: RegistryViewModel by viewModels()
    override val binding by viewBinding(FragmentRegistryBinding::bind)
    override fun getLayoutRes(): Int = R.layout.fragment_registry

    //region Overridden

    override fun setupViews() {
        etUsername = binding.etUsername
        etPassword = binding.etPassword
        loading = binding.loading
        btnMainAction = binding.btnRegister
        btnNavigation = binding.btnGoLogin
    }

    override fun onNavigationClicked() {
        findNavController().popBackStack(R.id.loginFragment, false)
    }

    //endregion
}