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

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToProperActivity()
    }

    //endregion

    //region Helpers

    private fun navigateToProperActivity() {
        val activity = if (FirebaseAuth.getInstance().currentUser != null)
            MainActivity::class.java
        else
            AuthActivity::class.java
        startActivity(Intent(this@SplashActivity, activity))
        finish()
    }

    //endregion
}