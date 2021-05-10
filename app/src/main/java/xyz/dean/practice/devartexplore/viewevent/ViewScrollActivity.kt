package xyz.dean.practice.devartexplore.viewevent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.dean.practice.devartexplore.R

class ViewScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_scroll)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ViewScrollActivity::class.java)
        }
    }
}