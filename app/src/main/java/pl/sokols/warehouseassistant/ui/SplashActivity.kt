package pl.sokols.warehouseassistant.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import pl.sokols.warehouseassistant.ui.auth.AuthActivity
import pl.sokols.warehouseassistant.ui.main.MainActivity

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfUserLoggedIn()
    }

    private fun checkIfUserLoggedIn() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
        }
        finish()
    }
}