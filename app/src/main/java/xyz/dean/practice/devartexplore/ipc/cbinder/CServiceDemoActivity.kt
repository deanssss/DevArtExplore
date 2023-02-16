package xyz.dean.practice.devartexplore.ipc.cbinder

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import xyz.dean.practice.devartexplore.R

class CServiceDemoActivity : AppCompatActivity() {
    private lateinit var cservice: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cservice_demo)

        findViewById<Button>(R.id.service_bt).setOnClickListener {
            bindService(
                CServiceDemoService.createIntent(this),
                object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName, service: IBinder) {
                        cservice = MyServiceImpl.asInterface(service)
                    }
                    override fun onServiceDisconnected(name: ComponentName) {
                        Log.e(TAG, "CServiceDemoService Disconnected.")
                    }
                },
                Context.BIND_AUTO_CREATE)
        }
        findViewById<Button>(R.id.client_bt).setOnClickListener {
            cservice.sayHello("hello~")
        }
    }

    companion object {
        private const val TAG = "CServiceDemoActivity"

        fun createIntent(context: Context) =
            Intent(context, CServiceDemoActivity::class.java)
    }
}