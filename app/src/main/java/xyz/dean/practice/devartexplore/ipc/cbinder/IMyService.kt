package xyz.dean.practice.devartexplore.ipc.cbinder

import android.os.IBinder
import android.os.IInterface
import android.os.RemoteException

interface IMyService : IInterface {
    @Throws(RemoteException::class)
    fun sayHello(msg: String)

    companion object {
        @JvmStatic
        val DESCRIPTOR = "xyz.dean.practice.devartexplore.ipc.cbinder.IMyService"
        @JvmStatic
        val TRANS_sayHello = IBinder.FIRST_CALL_TRANSACTION
    }
}