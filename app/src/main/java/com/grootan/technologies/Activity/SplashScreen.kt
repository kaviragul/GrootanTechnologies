package com.grootan.technologies.Activity

import androidx.appcompat.app.AppCompatActivity
import com.grootan.technologies.BroadCast.ConnectivityReceiver.ConnectivityReceiverListener
import com.grootan.technologies.Utils.SessionManagerSP
import android.os.Bundle
import com.grootan.technologies.R
import android.os.Build
import android.view.WindowManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Parcelable
import android.view.View
import com.grootan.technologies.Utils.CustomToast
import com.grootan.technologies.BroadCast.MyApplication
import java.lang.Exception

class SplashScreen : AppCompatActivity(), ConnectivityReceiverListener {
    var sessionManagerSP: SessionManagerSP? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            window.statusBarColor = this.resources.getColor(R.color.white)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        sessionManagerSP = SessionManagerSP(this@SplashScreen)
        showSnack()
    }

    private fun showSnack() {
        Handler().postDelayed({
            if (sessionManagerSP!!.getlogin() == "1") {
                val i = Intent(this@SplashScreen, HomeScreenActivity::class.java)
                startActivity(i)
                finish()
            } else {
                sessionManagerSP!!.setlogin("0")
                val i = Intent(this@SplashScreen, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }, SPLASH_TIME_OUT.toLong())
    }

    override fun onStop() {
        try {
            if (mConnReceiver != null) unregisterReceiver(mConnReceiver)
        } catch (e: Exception) {
        }
        super.onStop()
    }

    private val mConnReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val noConnectivity =
                intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            val reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON)
            val isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false)
            val currentNetworkInfo =
                intent.getParcelableExtra<Parcelable>(ConnectivityManager.EXTRA_NETWORK_INFO) as NetworkInfo?
            val otherNetworkInfo =
                intent.getParcelableExtra<Parcelable>(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO) as NetworkInfo?
            if (currentNetworkInfo!!.isConnected) {
            } else {
                CustomToast.getInstance(this@SplashScreen)
                    ?.showSmallCustomToast("Please check your internet connection")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // register connection status listener
        MyApplication.instance?.setConnectivityListener(this)
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {}

    companion object {
        private const val SPLASH_TIME_OUT = 2500
    }
}