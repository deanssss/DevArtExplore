package xyz.dean.practice.devartexplore.ipc

import android.content.*
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.EdgeEffect
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import xyz.dean.practice.devartexplore.R
import xyz.dean.practice.devartexplore.util.printWriter
import xyz.dean.practice.devartexplore.util.toast
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.PrintWriter
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.Socket
import java.util.concurrent.Executors

class IPCActivity : AppCompatActivity() {
    private var serviceMessenger: Messenger? = null
    private val messengerServiceConn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            toast("Service is disconnected.")
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            toast("Service is connected.")
            serviceMessenger = Messenger(service)
        }
    }

    private val handler = Handler()
    private var userManager: IUserManager? = null
    private val onNewUserListener = object : NewUserListener.Stub() {
        override fun onNewUserAdded(user: User?) {
            handler.post {
                toast("Add new user:($user) success.")
            }
        }
    }
    private val aidlServiceConn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            toast("Service is disconnected.")
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            toast("Service is connected.")
            userManager = IUserManager.Stub.asInterface(service).apply {
                registerNewUserListener(onNewUserListener)
            }
        }
    }

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private val executors = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_i_p_c)

        findViewById<Button>(R.id.bundle_ipc_bt).setOnClickListener {
            startService(BundleIPCService.createIntent(this, "Message from main process."))
        }

        val handler = Handler(Looper.getMainLooper())
        findViewById<Button>(R.id.read_data_bt).setOnClickListener {
            ShareFile.readData(this, handler) {
                toast("ReadData: ${it.toString()}")
            }
        }
        findViewById<Button>(R.id.file_ipc_bt).setOnClickListener {
            startService(FileIPCService.createIntent(this))
        }

        findViewById<Button>(R.id.messenger_ipc_bt).setOnClickListener {
            bindService(MessengerIPCService.createIntent(this), messengerServiceConn, Context.BIND_AUTO_CREATE)
        }
        findViewById<Button>(R.id.send_msg_bt).setOnClickListener {
            serviceMessenger?.let { messenger ->
                Bundle().apply {
                    putString(MessengerIPCService.KEY__CLIENT_MSG, "Message from client.")
                }.let {
                    Message.obtain(null, MessengerIPCService.CLIENT_MSG)
                        .apply {
                            data = it
                            replyTo = Messenger(MessengerHandler(WeakReference(this@IPCActivity)))
                        }
                }.also { messenger.send(it) }
            } ?: toast("Service is not started yet.")
        }

        findViewById<Button>(R.id.aidl_ipc_bt).setOnClickListener {
            bindService(AidlIPCService.createIntent(this), aidlServiceConn, Context.BIND_AUTO_CREATE)
        }
        findViewById<Button>(R.id.get_user_bt).setOnClickListener {
            Thread {
                val users = userManager?.userList
                handler.post {
                    toast("Get all users: ${users.toString()}")
                }
            }.start()
        }
        findViewById<Button>(R.id.add_user_bt).setOnClickListener {
            Thread {
                userManager?.addUser(User("wnagwu", 10))
            }.start()
        }

        val uri = Uri.parse("content://${UserDataProvider.AUTHORITY}")
        findViewById<Button>(R.id.query_all_bt).setOnClickListener {
            val userList = mutableListOf<User>()
            // Bad! IO in main thread.
            contentResolver.query(uri, arrayOf("_username", "_age"), null, null, null)
                    ?.use {
                        while (it.moveToNext()) {
                            userList.add(User(it.getString(0), it.getInt(1)))
                        }
                    }
            toast("Query all user: $userList")
        }
        findViewById<Button>(R.id.insert_user_bt).setOnClickListener {
            val values = ContentValues().apply {
                put("_username", "wangwu")
                put("_age", 27)
            }
            // Bad! IO in main thread.
            contentResolver.insert(uri, values)
        }

        findViewById<Button>(R.id.socket_ipc_bt).setOnClickListener {
            startService(SocketServerService.createIntent(this))
            Thread {
                SystemClock.sleep(3000)
                socket = Socket("localhost", 6666)
                handler.post { toast("Connect to server success!") }

                writer = socket.getOutputStream().writer().buffered().printWriter()
                reader = socket.getInputStream().reader().buffered()

                while (!socket.isClosed) {
                    try {
                        val msg = reader.readLine()
                        Log.d("Client", "Received message: $msg")
                        handler.post { toast(msg) }
                    } catch (e: Exception) {
                        Log.e("Client", "Error.", e)
                    }
                }

                handler.post { toast("Service is disconnected.") }
                writer.close()
                reader.close()
            }.start()
        }

        val msgEt = findViewById<EditText>(R.id.client_msg_et)
        findViewById<Button>(R.id.send_msg_to_server_bt).setOnClickListener {
            val msg = msgEt.text.toString()
            Log.d("Client", "Server state: ${socket.isConnected}")
            Log.d("Client", "Send message: $msg")
            executors.execute {
                writer.println(msg)
                writer.flush()
                if (msg == "/exit") {
                    socket.close()
                }
            }
        }

        findViewById<Button>(R.id.binder_pool_bt).setOnClickListener {
            startActivity(BinderPoolActivity.createIntent(this))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(messengerServiceConn)
    }

    private class MessengerHandler(val safeContext: WeakReference<Context>) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessengerIPCService.SERVICE_REPLY -> {
                    val serverReply = msg.data.getString(MessengerIPCService.KEY__SERVICE_REPLY, "No reply.")
                    safeContext.get()?.toast("Server Reply: $serverReply")
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    companion object {
        fun createIntent(ctx: Context): Intent =
            Intent(ctx, IPCActivity::class.java)
    }
}