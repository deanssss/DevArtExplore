package xyz.dean.practice.devartexplore.ipc.binderpool.server

import xyz.dean.practice.devartexplore.ipc.binderpool.ICompute

class ComputeImpl : ICompute.Stub() {
    override fun add(a: Int, b: Int): Int = a + b
}