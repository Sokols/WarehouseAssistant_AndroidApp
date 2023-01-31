package pl.sokols.warehouseassistant.ui.auth.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.ui.base.BaseFragment
import pl.sokols.warehouseassistant.ui.main.MainActivity
import pl.sokols.warehouseassistant.utils.AuthState
import pl.sokols.warehouseassistant.utils.AuthUtils.afterTextChanged

abstract class BaseAuthFragment : BaseFragment() {

    abstract fun setupViews()
    abstract fun onNavigationClicked()

    protected abstract val viewModel: BaseAuthViewModel

    protected lateinit var etUsername: TextInputEditText
    protected lateinit var etPassword: TextInputEditText
    protected lateinit var loading: ProgressBar
    protected lateinit var btnMainAction: MaterialButton
    protected lateinit var btnNavigation: Button

    //region Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setObservers()
        setupButtons()
        setupTextChangeListeners()
    }

    //endregion

    //region Setup methods

    private fun setupTextChangeListeners() {
        binding.apply {
            etUsername.afterTextChanged {
                viewModel.onInputDataChanged(
                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
            }

            etPassword.afterTextChanged {
                viewModel.onInputDataChanged(
                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
            }
        }
    }

    private fun setupButtons() {
        binding.apply {
            btnNavigation.setOnClickListener { onNavigationClicked() }
            btnMainAction.setOnClickListener {
                loading.visibility = View.VISIBLE
                onMainActionClicked()
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
                var textIdToDisplay: Int? = null
                when (state) {
                    AuthState.VALID -> btnMainAction.isEnabled = true
                    AuthState.PROVIDED_EMAIL_INVALID,
                    AuthState.PROVIDED_PASSWORD_TOO_SHORT,
                    AuthState.PROVIDED_PASSWORD_BLANK -> btnMainAction.isEnabled = false
                    AuthState.ERROR_WRONG_PASSWORD ->  textIdToDisplay = R.string.wrong_password
                    AuthState.ERROR_USER_NOT_FOUND ->  textIdToDisplay = R.string.user_not_found
                    AuthState.ERROR_OTHER ->  textIdToDisplay = R.string.other_error
                    AuthState.ERROR_EMAIL_ALREADY_IN_USE -> textIdToDisplay = R.string.email_in_use
                    else -> {}
                }
                textIdToDisplay?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
                loading.visibility = View.INVISIBLE
            }
        }
    }

    //endregion

    //region Helpers

    private fun onMainActionClicked() {
        viewModel.loginRegister(etUsername.text.toString(), etPassword.text.toString())
    }

    private fun goToMainActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    //endregion
}
