package com.grootan.technologies.BroadCast

import android.app.Application
import com.grootan.technologies.BroadCast.ConnectivityReceiver.ConnectivityReceiverListener
import kotlin.jvm.Synchronized

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setConnectivityListener(listener: ConnectivityReceiverListener?) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }
}