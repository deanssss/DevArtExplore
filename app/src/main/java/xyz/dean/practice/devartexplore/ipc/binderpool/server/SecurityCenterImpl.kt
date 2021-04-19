package xyz.dean.practice.devartexplore.ipc.binderpool.server

import android.util.Log
import xyz.dean.practice.devartexplore.ipc.binderpool.ISecurityCenter

class SecurityCenterImpl : ISecurityCenter.Stub() {
    private val SECRET_CODE = '^'.toInt()

    override fun encrypt(content: String?): String {
        Log.d("Security", "encrypt text: $content")
        val chs = content?.toCharArray() ?: charArrayOf()
        val echs = chs.map { it.toInt() xor SECRET_CODE }
            .map { it.toChar() }
            .toCharArray()
        return String(echs)
    }

    override fun decrypt(password: String?): String {
        Log.d("Security", "decrypt text: $password")
        return encrypt(password)
    }
}