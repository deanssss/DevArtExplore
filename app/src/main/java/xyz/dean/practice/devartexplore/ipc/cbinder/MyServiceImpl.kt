package xyz.dean.practice.devartexplore.ipc.cbinder

import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.util.Log

class MyServiceImpl : Binder(), IMyService {
    init {
        attachInterface(this, IMyService.DESCRIPTOR)
    }

    override fun sayHello(msg: String) {
        Log.d(TAG, "rec msg: $msg")
    }

    override fun asBinder(): IBinder = this

    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        when (code) {
            INTERFACE_TRANSACTION -> {
                Log.d(TAG, "INTERFACE_TRANSACTION")
                reply?.writeString(IMyService.DESCRIPTOR)
                return true
            }
            IMyService.TRANS_sayHello -> {
                Log.d(TAG, "TRANS_sayHello")
                data.enforceInterface(IMyService.DESCRIPTOR)
                val str = data.readString()!!
                sayHello(str)
                reply?.writeNoException()
                return true
            }
        }
        return super.onTransact(code, data, reply, flags)
    }

    companion object {
        private const val TAG = "MyService"

        @JvmStatic
        fun asInterface(binder: IBinder): IMyService {
            val localService = binder.queryLocalInterface(IMyService.DESCRIPTOR) as? IMyService
            return if (localService != null) {
                Log.d(TAG, "get service in same process.")
                localService
            } else {
                Log.d(TAG, "get service from another process.")
                MyServiceProxy(binder)
            }
        }
    }
}