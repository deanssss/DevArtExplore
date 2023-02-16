package xyz.dean.practice.devartexplore.ipc.cbinder

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class CServiceDemoService : Service() {
    override fun onBind(intent: Intent): IBinder = MyServiceImpl()

    companion object {
        fun createIntent(context: Context) = Intent(context, CServiceDemoService::class.java)
    }
}