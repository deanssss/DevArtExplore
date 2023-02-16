package xyz.dean.practice.devartexplore.ipc.cbinder

import android.os.IBinder
import android.os.Parcel

class MyServiceProxy(val remote: IBinder) : IMyService {
    override fun asBinder(): IBinder = remote

    val interfaceDescriptor get() = IMyService.DESCRIPTOR

    override fun sayHello(msg: String) {
        val _data = Parcel.obtain()
        val _reply = Parcel.obtain()

        try {
            _data.writeInterfaceToken(interfaceDescriptor)
            _data.writeString(msg)
            remote.transact(IMyService.TRANS_sayHello, _data, _reply, 0)
            _reply.readException()
        } finally {
            _reply.recycle()
            _data.recycle()
        }
    }
}