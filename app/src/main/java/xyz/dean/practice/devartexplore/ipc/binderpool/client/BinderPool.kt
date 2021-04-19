package xyz.dean.practice.devartexplore.ipc.binderpool.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.IInterface
import xyz.dean.practice.devartexplore.ipc.binderpool.IBinderPool
import xyz.dean.practice.devartexplore.ipc.binderpool.ServiceType
import java.util.concurrent.CountDownLatch

class BinderPool {
    lateinit var serviceBinder: IBinderPool

    private lateinit var countDownLatch: CountDownLatch

    @Synchronized
    fun connectBinderPoolService(context: Context) {
        countDownLatch = CountDownLatch(1)
        val intent = Intent("xyz.dean.practice.devartexplore.binderpool")
            .setPackage("xyz.dean.practice.devartexplore")
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE)
        countDownLatch.await()
    }

    fun <T : IInterface> queryBinder(@ServiceType binderCode: Int, a: (IBinder) -> T?): T? {
        return serviceBinder.takeIf { this::serviceBinder.isInitialized }
            ?.queryBinder(binderCode)
            ?.let { a(it) }
    }

    private val conn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = IBinderPool.Stub.asInterface(service)
            countDownLatch.countDown()
        }
    }

    companion object {
        val instance: BinderPool by lazy { BinderPool() }
    }
}