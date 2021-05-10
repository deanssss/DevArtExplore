package xyz.dean.practice.devartexplore.viewevent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import xyz.dean.practice.devartexplore.R

class ViewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)

        findViewById<Button>(R.id.view_scroll_bt).setOnClickListener {
            startActivity(ViewScrollActivity.createIntent(this))
        }
        findViewById<Button>(R.id.nested_cross_scroll_outer_intercept_bt).setOnClickListener {
            startActivity(NestedCrossScrollActivityOuterIntercept.createIntent(this))
        }
        findViewById<Button>(R.id.nested_cross_scroll_inner_intercept_bt).setOnClickListener {
            startActivity(NestedCrossScrollActivityInnerIntercept.createIntent(this))
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ViewEventActivity::class.java)
        }
    }
}