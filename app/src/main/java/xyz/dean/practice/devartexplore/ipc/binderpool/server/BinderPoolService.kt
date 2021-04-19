package xyz.dean.practice.devartexplore.ipc.binderpool.server

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BinderPoolService : Service() {
    private val binderPool = BinderPoolImpl()

    override fun onBind(intent: Intent): IBinder = binderPool
}