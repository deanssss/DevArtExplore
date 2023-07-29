package xyz.dean.practice.devartexplore

import android.app.Application
import android.util.Log

class MainApplication : Application() {
    private val TAG = "MainApplication"

    init {
        println("init application.")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(TAG, "Application start. $this")
    }

    companion object {
        @JvmField
        var instance: MainApplication? = null
    }
}