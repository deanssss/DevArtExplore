package xyz.dean.practice.devartexplore.ipc

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import xyz.dean.practice.devartexplore.util.toast

class BundleIPCService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val msg = getExtraMsg(intent!!)
        toast(msg)
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val EXTRA_KEY__MSG = "msg"

        fun createIntent(ctx: Context, msg: String): Intent =
            Intent(ctx, BundleIPCService::class.java)
                .putExtra(EXTRA_KEY__MSG, msg)

        private fun getExtraMsg(intent: Intent): String =
            intent.getStringExtra(EXTRA_KEY__MSG) ?: ""
    }
}