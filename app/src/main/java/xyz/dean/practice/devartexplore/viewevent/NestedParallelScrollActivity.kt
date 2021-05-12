package xyz.dean.practice.devartexplore.viewevent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import xyz.dean.practice.devartexplore.R

class NestedParallelScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_parallel_scroll)

        val listLv = findViewById<ListView>(R.id.sticky_content)
        val data = (0 .. 50).map { "name $it" }
        val adapter = ArrayAdapter<String>(this, R.layout.nested_list_item_layout, R.id.name_tv, data)
        listLv.adapter = adapter

        val stickyLayout = findViewById<StickyLayout>(R.id.sticky_layout)
        stickyLayout.onContentGiveUpEvent = {
            if (listLv.firstVisiblePosition == 0) {
                val v = listLv.getChildAt(0)
                v != null && v.top >= 0
            } else {
                false
            }
        }
    }

    companion object {
        fun createIntent(context: Context) =
            Intent(context, NestedParallelScrollActivity::class.java)
    }
}