package xyz.dean.practice.devartexplore.ipc

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.util.Log
import xyz.dean.practice.devartexplore.util.printWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class SocketServerService : Service() {
    private val server = Server()
    override fun onCreate() {
        super.onCreate()
        Thread(server).start()
    }

    override fun onBind(intent: Intent): Binder? = null

    override fun onDestroy() {
        super.onDestroy()
        server.isServiceDestroyed = true
        Socket("localhost", 6666)
            .getOutputStream()
            .writer()
            .buffered()
            .printWriter()
            .use { it.print("/exit") }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SocketServerService::class.java)
        }
    }
    private class Server : Runnable {
        var isServiceDestroyed = false
        private val executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)

        override fun run() {
            val serverSocket = ServerSocket(6666)

            while (!isServiceDestroyed) {
                val clientSocket = serverSocket.accept()
                executors.submit(ClientConnection(clientSocket))
            }
        }
    }

    private class ClientConnection(val clientSocket: Socket) : Runnable {
        override fun run() {
            val reader = clientSocket.getInputStream().reader().buffered()
            val writer = clientSocket.getOutputStream().writer().buffered().printWriter()

            writer.println("欢迎使用复读机服务！ /exit 退出")
            writer.flush()
            Log.d("Server", "Connection established!")
            while (true) {
                val clientMsg = reader.readLine()
                Log.d("Server", "Received message: $clientMsg")
                if (clientMsg == "/exit") {
                    break
                }
                writer.println("Server: $clientMsg")
                writer.flush()
            }

            reader.close()
            writer.close()
        }
    }
}