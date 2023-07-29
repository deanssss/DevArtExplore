package xyz.dean.practice.common

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    open val tag: String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d(tag, "=== onCreate ===")
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(tag, "=== onSaveInstanceState ===")
        super.onSaveInstanceState(outState)
    }

    override fun onRestart() {
        Log.d(tag, "=== onRestart ===")
        super.onRestart()
    }

    override fun onStart() {
        Log.d(tag, "=== onStart === ")
        super.onStart()
    }

    override fun onResume() {
        Log.d(tag, "=== onResume ===")
        super.onResume()
    }

    override fun onPause() {
        Log.d(tag, "=== onPause ===")
        super.onPause()
    }

    override fun onStop() {
        Log.d(tag, "=== onStop ===")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(tag, "=== onDestroy ===")
        super.onDestroy()
    }
}