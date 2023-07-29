package xyz.dean.practice.target

import android.app.Application
import android.util.Log

class TargetApplication : Application() {
    private val TAG = "TargetApplication"

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
        var instance: TargetApplication? = null
    }
}