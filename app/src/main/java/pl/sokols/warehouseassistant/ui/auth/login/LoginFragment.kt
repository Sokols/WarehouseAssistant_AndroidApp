package pl.sokols.warehouseassistant.ui.auth.login

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.FragmentLoginBinding
import pl.sokols.warehouseassistant.ui.auth.base.BaseAuthFragment
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class LoginFragment : BaseAuthFragment() {

    override val viewModel: LoginViewModel by viewModels()
    override val binding by viewBinding(FragmentLoginBinding::bind)
    override fun getLayoutRes(): Int = R.layout.fragment_login

    //region Overridden

    override fun setupViews() {
        etUsername = binding.etUsername
        etPassword = binding.etPassword
        loading = binding.loading
        btnMainAction = binding.btnLogin
        btnNavigation = binding.btnRegistry
    }

    override fun onNavigationClicked() {
        val directions = LoginFragmentDirections.actionLoginFragmentToRegistryFragment()
        findNavController().navigate(directions)
    }

    //endregion
}
