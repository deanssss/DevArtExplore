package xyz.dean.practice.devartexplore.ipc

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import xyz.dean.practice.devartexplore.util.broadcastAll
import java.util.concurrent.CopyOnWriteArrayList

class AidlIPCService : Service() {
    private val users = CopyOnWriteArrayList<User>()
    private val listeners = RemoteCallbackList<NewUserListener>()

    private val binder = object : IUserManager.Stub() {
        override fun getUserList(): MutableList<User> = users
        override fun addUser(user: User?) {
            users.add(user)
            listeners.broadcastAll {
                it.onNewUserAdded(user)
            }
        }

        override fun registerNewUserListener(listener: NewUserListener?) {
            listeners.register(listener)
        }
        override fun unregisterNewUserListener(listener: NewUserListener?) {
            listeners.unregister(listener)
        }
    }

    override fun onCreate() {
        super.onCreate()
        users.add(User("zhanhsan", 25))
        users.add(User("lisi", 26))
    }

    override fun onBind(intent: Intent): IBinder = binder

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AidlIPCService::class.java)
        }
    }
}