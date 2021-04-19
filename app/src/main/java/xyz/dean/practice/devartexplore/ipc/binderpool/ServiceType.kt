package xyz.dean.practice.devartexplore.ipc.binderpool

import androidx.annotation.IntDef

@IntDef(value = [BINDER_COMPUTE, BINDER_SECURITY_CENTER])
@Retention(AnnotationRetention.SOURCE)
annotation class ServiceType

const val BINDER_COMPUTE = 0
const val BINDER_SECURITY_CENTER = 1
