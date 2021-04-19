package xyz.dean.practice.devartexplore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import xyz.dean.practice.devartexplore.ipc.IPCActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.ipc_bt).setOnClickListener {
            startActivity(IPCActivity.createIntent(this))
        }
    }
}