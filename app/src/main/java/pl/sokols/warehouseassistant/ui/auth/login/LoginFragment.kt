package pl.sokols.warehouseassistant.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
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
        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val goToRegistry = binding.goToRegistry

        goToRegistry.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_registryFragment)
        }

        username.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.login(username.text.toString(), password.text.toString())
        }

        viewModel.authFormState.observe(viewLifecycleOwner, { state ->
            when (state) {
                AuthState.VALID -> login.isEnabled = true
                AuthState.PROVIDED_EMAIL_INVALID,
                AuthState.PROVIDED_PASSWORD_BLANK -> login.isEnabled = false
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
        })
    }

    private fun setObservers() {
        viewModel.userLiveData.observe(viewLifecycleOwner, { user ->
            if (user != null) {
                goToMainActivity()
            }
        })
    }

    private fun goToMainActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}
