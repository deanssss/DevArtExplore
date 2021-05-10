package xyz.dean.practice.devartexplore.viewevent

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import xyz.dean.practice.devartexplore.R

@SuppressLint("SetTextI18n")
class NestedCrossScrollActivityOuterIntercept : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_cross_scroll_outer_intercept)

        val hScrollView = findViewById<HorizontalScrollViewExOuterIntercept>(R.id.container)
        val screenSize = Point()
        windowManager.defaultDisplay.getSize(screenSize)
        val screenWidth = screenSize.x

        for (i in 0 .. 3) {
            val layout = layoutInflater.inflate(R.layout.nested_outer_intercept_layout, hScrollView, false) as ViewGroup
            layout.layoutParams.width = screenWidth
            val titleTv = layout.findViewById<TextView>(R.id.title_tv)
            titleTv.text = "Page ${i + 1}"
            layout.setBackgroundColor(Color.rgb(255/(i + 1), 255/(i + 1), 0))
            createList(layout)
            hScrollView.addView(layout)
        }
    }

    private fun createList(layout: ViewGroup) {
        val listLv = layout.findViewById<ListView>(R.id.list_lv)
        val data = (0 .. 50).map { "name $it" }
        val adapter = ArrayAdapter<String>(this, R.layout.nested_list_item_layout, R.id.name_tv, data)
        listLv.adapter = adapter
    }

    companion object {
        fun createIntent(context: Context) =
            Intent(context, NestedCrossScrollActivityOuterIntercept::class.java)
    }
}