package pl.sokols.warehouseassistant.ui.main

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ACTION_NDEF_DISCOVERED
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.MainActivityBinding
import pl.sokols.warehouseassistant.ui.auth.AuthActivity
import pl.sokols.warehouseassistant.utils.NFCUtil

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var nfcAdapter: NfcAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        setContentView(binding.root)
        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        NFCUtil.enableNFCInForeground(nfcAdapter, this, javaClass)
    }

    override fun onPause() {
        super.onPause()
        NFCUtil.disableNFCInForeground(nfcAdapter, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.action = ACTION_NDEF_DISCOVERED;
        val messageWrittenSuccessfully = viewModel.retrieveNFCMessage(intent)
        Toast.makeText(
            this,
            messageWrittenSuccessfully,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupToolbar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.itemsFragment,
                R.id.inventoryFragment
            ),
            binding.drawerLayout
        )
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
        binding.drawerView.setupWithNavController(navController)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    viewModel.logout()
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}