package xyz.dean.practice.devartexplore.component.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import xyz.dean.practice.devartexplore.R

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        findViewById<View>(R.id.launch_standard).setOnClickListener {
            startActivity(StandardActivity.createIntent(this))
        }
        findViewById<View>(R.id.launch_implicit).setOnClickListener {
            startActivity(Intent("xyz.dean.practice.target"))
        }
    }

    companion object {
        fun createIntent(context: Context) =
            Intent(context, LauncherActivity::class.java)
    }
}