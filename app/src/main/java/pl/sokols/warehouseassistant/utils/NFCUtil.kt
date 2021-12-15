package pl.sokols.warehouseassistant.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.ui.main.screens.inventory.procedure.InventoryProcedureFragment
import pl.sokols.warehouseassistant.ui.main.screens.items.ItemsFragment

object NFCUtil {

    fun disableNFCInForeground(nfcAdapter: NfcAdapter?, activity: Activity) {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun <T> enableNFCInForeground(
        nfcAdapter: NfcAdapter?,
        activity: Activity,
        classType: Class<T>
    ) {
        val pendingIntent = PendingIntent.getActivity(
            activity, 0,
            Intent(activity, classType).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val nfcIntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val filters = arrayOf(nfcIntentFilter)

        val techLists =
            arrayOf(arrayOf(Ndef::class.java.name), arrayOf(NdefFormatable::class.java.name))

        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, filters, techLists)
    }

    fun retrieveIntent(supportFragmentManager: FragmentManager, intent: Intent?) {
        val navHost = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                // Propagate intent only on specified fragments
                when (fragment) {
                    is ItemsFragment -> fragment.retrieveIntent(intent)
                    is InventoryProcedureFragment -> fragment.retrieveIntent(intent)
                }
            }
        }
    }

    fun displayToast(context: Context?, nfcData: NfcState) {
        val message = when (nfcData) {
            NfcState.WRITTEN_TO_THE_TAG -> context?.getString(R.string.written_to_the_tag)
            NfcState.CANNOT_FIND_ITEM -> context?.getString(R.string.cannot_find_item)
            else -> context?.getString(R.string.other_error)
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

enum class NfcState {
    READ,
    WRITE,
    WRITTEN_TO_THE_TAG,
    CANNOT_FIND_ITEM,
    ERROR
}