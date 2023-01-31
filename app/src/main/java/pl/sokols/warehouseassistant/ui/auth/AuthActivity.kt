package pl.sokols.warehouseassistant.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.databinding.AuthActivityBinding
import pl.sokols.warehouseassistant.databinding.MainActivityBinding

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: AuthActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}