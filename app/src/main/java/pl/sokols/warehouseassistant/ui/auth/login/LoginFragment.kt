package pl.sokols.warehouseassistant.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.LoginFragmentBinding
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.MainActivity
import pl.sokols.warehouseassistant.utils.AuthState
import pl.sokols.warehouseassistant.utils.AuthUtils.afterTextChanged
import pl.sokols.warehouseassistant.utils.viewBinding

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModels()
    override val binding by viewBinding(LoginFragmentBinding::bind)
    override fun getLayoutRes(): Int = R.layout.login_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setupButtons()
        setupTextChangeListeners()
    }

    private fun setupTextChangeListeners() {
        binding.apply {
            etUsername.afterTextChanged {
                viewModel.loginDataChanged(
                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
            }

            etPassword.afterTextChanged {
                viewModel.loginDataChanged(
                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
            }
        }
    }

    private fun setupButtons() {
        binding.apply {
            btnRegistry.setOnClickListener {
                val directions = LoginFragmentDirections.actionLoginFragmentToRegistryFragment()
                findNavController().navigate(directions)
            }

            btnLogin.setOnClickListener {
                loading.visibility = View.VISIBLE
                viewModel.login(etUsername.text.toString(), etPassword.text.toString())
            }
        }
    }

    private fun setObservers() {
        binding.apply {
            viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    goToMainActivity()
                }
            }

            viewModel.authFormState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    AuthState.VALID -> btnLogin.isEnabled = true
                    AuthState.PROVIDED_EMAIL_INVALID,
                    AuthState.PROVIDED_PASSWORD_BLANK -> btnLogin.isEnabled = false
                    AuthState.ERROR_WRONG_PASSWORD -> {
                        Toast.makeText(context, R.string.wrong_password, Toast.LENGTH_SHORT).show()
                        loading.visibility = View.INVISIBLE
                    }
                    AuthState.ERROR_USER_NOT_FOUND -> {
                        Toast.makeText(context, R.string.user_not_found, Toast.LENGTH_SHORT).show()
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
    }

    private fun goToMainActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}
