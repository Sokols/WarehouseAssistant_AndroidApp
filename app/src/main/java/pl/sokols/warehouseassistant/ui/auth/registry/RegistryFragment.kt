package pl.sokols.warehouseassistant.ui.auth.registry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.RegistryFragmentBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.MainActivity
import pl.sokols.warehouseassistant.utils.AuthState
import pl.sokols.warehouseassistant.utils.AuthUtils.afterTextChanged
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class RegistryFragment : BaseFragment() {

    private val viewModel: RegistryViewModel by viewModels()
    override val binding by viewBinding(RegistryFragmentBinding::bind)
    override fun getLayoutRes(): Int = R.layout.registry_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()

        val username = binding.etUsername
        val password = binding.etPassword
        val register = binding.register
        val loading = binding.loading
        val goToLogin = binding.goToLogin

        goToLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_registryFragment_to_loginFragment)
        }

        username.afterTextChanged {
            viewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            viewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        register.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.register(username.text.toString(), password.text.toString())
        }

        viewModel.authFormState.observe(viewLifecycleOwner) { state ->
            when (state) {
                AuthState.VALID -> register.isEnabled = true
                AuthState.PROVIDED_EMAIL_INVALID,
                AuthState.PROVIDED_PASSWORD_TOO_SHORT -> register.isEnabled = false
                AuthState.ERROR_EMAIL_ALREADY_IN_USE -> {
                    Toast.makeText(context, R.string.email_in_use, Toast.LENGTH_SHORT).show()
                    loading.visibility = View.INVISIBLE
                }
                AuthState.ERROR_OTHER -> {
                    Toast.makeText(context, R.string.other_error, Toast.LENGTH_SHORT).show()
                    loading.visibility = View.INVISIBLE
                }
                else -> {}
            }
        }
    }

    private fun setObservers() {
        viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                goToMainActivity()
            }
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}