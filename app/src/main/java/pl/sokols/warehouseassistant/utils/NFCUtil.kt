package pl.sokols.warehouseassistant.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import androidx.fragment.app.FragmentManager
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.ui.main.items.ItemsFragment
import java.io.IOException

object NFCUtil {

    fun disableNFCInForeground(nfcAdapter: NfcAdapter?, activity: Activity) {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    fun <T> enableNFCInForeground(nfcAdapter: NfcAdapter?, activity: Activity, classType: Class<T>) {
        val pendingIntent = PendingIntent.getActivity(activity, 0,
                Intent(activity, classType).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val nfcIntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val filters = arrayOf(nfcIntentFilter)

        val techLists = arrayOf(arrayOf(Ndef::class.java.name), arrayOf(NdefFormatable::class.java.name))

        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, filters, techLists)
    }

    fun retrieveIntent(supportFragmentManager: FragmentManager, intent: Intent?) {
        val navHost = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                if (fragment is ItemsFragment) {
                    fragment.retrieveIntent(intent)
                }
            }
        }
    }
}

enum class NfcState {
    READ,
    WRITE,
    WRITTEN_TO_THE_TAG,
    CANNOT_FIND_ITEM,
    ERROR
}