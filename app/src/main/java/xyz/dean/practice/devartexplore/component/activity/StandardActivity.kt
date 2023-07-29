package xyz.dean.practice.devartexplore.component.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.dean.practice.devartexplore.R

class StandardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standard)
//        Log.d("DDDDDD", "app: " + MainApplication.instance.toString())
    }

    companion object {
        fun createIntent(context: Context) =
            Intent(context, StandardActivity::class.java)
    }
}