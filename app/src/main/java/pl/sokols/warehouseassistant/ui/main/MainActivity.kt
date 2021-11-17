package pl.sokols.warehouseassistant.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.MainActivityBinding
import pl.sokols.warehouseassistant.ui.auth.AuthActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
    }

    private fun setupToolbar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.itemsFragment
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