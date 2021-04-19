package xyz.dean.practice.devartexplore.ipc.binderpool.server

import android.os.IBinder
import xyz.dean.practice.devartexplore.ipc.binderpool.BINDER_COMPUTE
import xyz.dean.practice.devartexplore.ipc.binderpool.BINDER_SECURITY_CENTER
import xyz.dean.practice.devartexplore.ipc.binderpool.IBinderPool
import xyz.dean.practice.devartexplore.ipc.binderpool.ServiceType

class BinderPoolImpl : IBinderPool.Stub() {
    override fun queryBinder(@ServiceType binderCode: Int): IBinder? {
        return when (binderCode) {
            BINDER_COMPUTE -> ComputeImpl()
            BINDER_SECURITY_CENTER -> SecurityCenterImpl()
            else -> null
        }
    }
}