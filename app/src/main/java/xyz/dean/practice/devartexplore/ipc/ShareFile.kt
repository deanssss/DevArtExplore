package xyz.dean.practice.devartexplore.ipc

import android.content.Context
import android.os.Handler
import xyz.dean.practice.devartexplore.util.objectOutputStream
import xyz.dean.practice.devartexplore.util.objectInputStream
import java.io.File
import java.lang.Exception

object ShareFile {
    private const val IPC_FILE_NAME = "data.ipc"

    fun saveData(ctx: Context, user: User, handler: Handler, onResult: (Boolean) -> Unit) {
        Thread {
            val result = try {
                File(ctx.filesDir, IPC_FILE_NAME).outputStream()
                    .objectOutputStream()
                    .use { it.writeObject(user) }
                true
            } catch (e: Exception) {
                false
            }
            handler.post { onResult(result) }
        }.start()
    }

    fun readData(ctx: Context, handler: Handler, onResult: (User?) -> Unit) {
        Thread {
            val user = try {
                val file = File(ctx.filesDir, IPC_FILE_NAME)
                if (file.exists()) {
                    file.inputStream()
                        .objectInputStream()
                        .use { it.readObject() as User }
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
            handler.post { onResult(user) }
        }.start()
    }
}