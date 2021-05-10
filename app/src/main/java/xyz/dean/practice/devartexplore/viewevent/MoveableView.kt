package xyz.dean.practice.devartexplore.viewevent

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

class MoveableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private var lastX = 0f
    private var lastY = 0f

    private var touchedX = 0f
    private var touchedY = 0f
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchedX = x
                touchedY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                val deltaY = y - lastY
                translationX += deltaX
                translationY += deltaY
            }
            MotionEvent.ACTION_UP -> {
                val deltaX = x - touchedX
                val deltaY = y - touchedY
                if (abs(deltaX) < touchSlop && abs(deltaY) < touchSlop) {
                    performClick()
                }
            }
        }

        lastX = x
        lastY = y
        return true
    }

    override fun performClick(): Boolean {
        Log.d("MoveableView", "perform single click.")
        return super.performClick()
    }
}