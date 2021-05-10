package xyz.dean.practice.devartexplore.viewevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ListView
import xyz.dean.practice.devartexplore.util.component1
import xyz.dean.practice.devartexplore.util.component2
import xyz.dean.practice.devartexplore.util.component3
import kotlin.math.abs

class ListViewEx @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ListView(context, attrs, defStyle) {
    private var lastX = 0
    private var lastY = 0

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val (x, y, c) = ev
        when (c) {
            MotionEvent.ACTION_DOWN -> {
                // 禁用父view的默认拦截
                disallowParentInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                val deltaY = y - lastY
                if (abs(deltaX) > abs(deltaY)) {
                    // 子view决定不拦截之后的事件了，启用父view的默认拦截，来处理后续事件
                    // 此方式与外部拦截的区别在于，子view即使已经处理过一段时间的滚动了，后续事件还是能被父view拦截掉
                    //    从而造成不好的使用体验
                    disallowParentInterceptTouchEvent(false)
                }
            }
        }
        lastX = x
        lastY = y
        return super.dispatchTouchEvent(ev)
    }

    private fun disallowParentInterceptTouchEvent(disallowIntercept: Boolean) {
        val parent = parent
        if (parent is ViewGroup) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept)
        }
    }
}