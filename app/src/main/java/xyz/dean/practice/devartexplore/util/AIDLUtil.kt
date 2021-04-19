package xyz.dean.practice.devartexplore.util

import android.os.IInterface
import android.os.RemoteCallbackList

inline fun <reified T : IInterface> RemoteCallbackList<T>.broadcastAll(action: (T) -> Unit) {
    val size = beginBroadcast()
    for (i in 0 until size)
        action(getBroadcastItem(i))
    finishBroadcast()
}