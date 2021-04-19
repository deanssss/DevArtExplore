package xyz.dean.practice.devartexplore.ipc

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import xyz.dean.practice.devartexplore.util.toast
import java.util.concurrent.atomic.AtomicInteger

class FileIPCService : Service() {
    private val age = AtomicInteger(1)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val handler = Handler(Looper.getMainLooper())
        val user = User("RemoteUser", age.getAndIncrement())
        ShareFile.saveData(this, user, handler) {
            toast("Save data ${if (it) "success. $user" else "failure."}")
        }

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        fun createIntent(ctx: Context): Intent =
            Intent(ctx, FileIPCService::class.java)
    }
}