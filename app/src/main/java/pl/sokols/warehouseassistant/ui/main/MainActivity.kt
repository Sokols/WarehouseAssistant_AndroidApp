package pl.sokols.warehouseassistant.ui.main

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ACTION_NDEF_DISCOVERED
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.MainActivityBinding
import pl.sokols.warehouseassistant.ui.auth.AuthActivity
import pl.sokols.warehouseassistant.utils.NFCUtil
import pl.sokols.warehouseassistant.utils.Utils

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: MainActivityBinding
    private var nfcAdapter: NfcAdapter? = null
    private val viewModel: MainViewModel by viewModels()

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    override fun onResume() {
        super.onResume()
        setPlaceholderView()
        NFCUtil.enableNFCInForeground(nfcAdapter, this, javaClass)
    }

    override fun onPause() {
        super.onPause()
        NFCUtil.disableNFCInForeground(nfcAdapter, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.action = ACTION_NDEF_DISCOVERED
        NFCUtil.retrieveIntent(supportFragmentManager, intent)
    }

    //endregion

    //region Helpers

    private fun setup() {
        binding = MainActivityBinding.inflate(LayoutInflater.from(this))
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        navController =
            (supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment).navController
        setContentView(binding.root)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.apply {
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                    R.id.itemsFragment,
                    R.id.inventoryFragment
                ),
                drawerLayout
            )
            topAppBar.setupWithNavController(navController, appBarConfiguration)
            drawerView.setupWithNavController(navController)

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.logout -> {
                        displayLogoutDialog()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setPlaceholderView() {
        binding.apply {
            val isMainViewVisible = checkIfInternetAndNfcEnabled()
            drawerLayout.isVisible = isMainViewVisible
            noServicesPlaceholder.isVisible = !isMainViewVisible
            refreshButton.setOnClickListener {
                finish()
                startActivity(intent)
            }
        }
    }

    private fun checkIfInternetAndNfcEnabled(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            val isInternetEnabled = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            val isNfcEnabled = nfcAdapter != null && NfcAdapter.getDefaultAdapter(this).isEnabled
            return isInternetEnabled && isNfcEnabled
        }
        return false
    }

    private fun displayLogoutDialog() {
        Utils.displayLogoutDialog(this, getString(R.string.are_you_sure_to_logout)) { _, _ ->
            viewModel.logout()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }.show()
    }

    //endregion
}