package com.example.bankholiday

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.example.bankholiday.broadcastReceiver.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var mSnackBar: Snackbar? = null
    private val internetBroadCastReceiver = ConnectivityReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPostResume() {
        super.onPostResume()
        registerReceiver(
            internetBroadCastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        ConnectivityReceiver.connectivityReceiverListener = this
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetBroadCastReceiver)
    }
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            mSnackBar = Snackbar
                .make(findViewById(R.id.parent), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Turn Wifi on") {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                .setActionTextColor(ContextCompat.getColor(this, R.color.white))
            mSnackBar?.show()
        } else {
            mSnackBar?.dismiss()
        }
    }
}