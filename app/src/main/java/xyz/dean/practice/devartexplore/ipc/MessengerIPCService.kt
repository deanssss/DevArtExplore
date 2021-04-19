package xyz.dean.practice.devartexplore.ipc

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import xyz.dean.practice.devartexplore.util.toast
import java.lang.ref.WeakReference

class MessengerIPCService : Service() {
    private val messenger = Messenger(MessengerHandler(WeakReference(this)))

    override fun onBind(intent: Intent): IBinder = messenger.binder

    private class MessengerHandler(val safeContext: WeakReference<Context>) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                CLIENT_MSG -> {
                    val clientMsg = msg.data.getString(KEY__CLIENT_MSG, "Empty Message.")
                    safeContext.get()?.toast("Client: $clientMsg")

                    val clientMessenger = msg.replyTo
                    clientMessenger?.let { messenger ->
                        Bundle().apply {
                            putString(KEY__SERVICE_REPLY, "Service reply for client msg: '$clientMsg'.")
                        }.let {
                            Message.obtain(null, SERVICE_REPLY)
                                .apply {
                                    data = it
                                }
                        }.also { messenger.send(it) }
                    } ?: safeContext.get()?.toast("Client is not provide reply messenger.")
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    companion object {
        const val CLIENT_MSG = 0x100
        const val SERVICE_REPLY = 0x101
        const val KEY__CLIENT_MSG = "msg"
        const val KEY__SERVICE_REPLY = "reply"

        fun createIntent(context: Context): Intent {
            return Intent(context, MessengerIPCService::class.java)
        }
    }
}