package xyz.dean.practice.devartexplore

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import xyz.dean.practice.devartexplore.component.activity.LauncherActivity
import xyz.dean.practice.devartexplore.ipc.IPCActivity
import xyz.dean.practice.devartexplore.viewevent.ViewEventActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.ipc_bt).setOnClickListener {
            startActivity(IPCActivity.createIntent(this))
        }
        findViewById<Button>(R.id.view_event_bt).setOnClickListener {
            startActivity(ViewEventActivity.createIntent(this))
        }
        findViewById<Button>(R.id.launch_mode_bt).setOnClickListener {
            startActivity(LauncherActivity.createIntent(this))
        }
    }
}