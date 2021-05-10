package xyz.dean.practice.devartexplore.viewevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import xyz.dean.practice.devartexplore.util.component1
import xyz.dean.practice.devartexplore.util.component2
import xyz.dean.practice.devartexplore.util.component3
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class HorizontalScrollViewExInnerIntercept @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {
    private var childrenSize: Int = 0
    private var childrenWidth: Int = 0
    private var childrenIndex: Int = 0

    private var lastX: Int = 0
    private var lastY: Int = 0

    private val scroller = Scroller(context)
    private val velocityTracker = VelocityTracker.obtain()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measureWith = 0
        var measureHeight = 0
        val childCount = childCount

        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val (widthSpecMode, widthSpaceSize) = widthMeasureSpec
        val (heightSpecMode, heightSpaceSize) = heightMeasureSpec

        if (childCount == 0) {
            setMeasuredDimension(0, 0)
        } else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measureWith = childView.measuredWidth * childCount
            measureHeight = childView.measuredHeight
            setMeasuredDimension(measureWith, measureHeight)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measureHeight = childView.measuredHeight
            setMeasuredDimension(widthSpaceSize, measureHeight)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measureWith = childView.measuredWidth * childCount
            setMeasuredDimension(measureWith, heightSpaceSize)
        } else {
            setMeasuredDimension(widthSpaceSize, heightSpaceSize)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        val childCount = childCount
        childrenSize = childCount

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE) {
                val childWidth = childView.measuredWidth
                childrenWidth = childWidth
                childView.layout(childLeft, 0, childLeft + childWidth, childView.measuredHeight)
                childLeft += childWidth
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val (x, y, c) = ev
        return when (c) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                    true
                } else {
                    false
                }
            }
            else -> true
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        velocityTracker.addMovement(event)

        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                scrollBy(-deltaX, 0)
            }
            MotionEvent.ACTION_UP -> {
                val scrollX = scrollX
                velocityTracker.computeCurrentVelocity(1000)
                val velocity = velocityTracker.xVelocity

                @Suppress("LiftReturnOrAssignment")
                if (abs(velocity) >= 50) {
                    childrenIndex = if (velocity > 0) childrenIndex - 1 else childrenIndex + 1
                } else {
                    childrenIndex = (scrollX + childrenWidth / 2) / childrenWidth
                }
                childrenIndex = max(0, min(childrenIndex, childrenSize - 1))

                val dx = childrenIndex * childrenWidth - scrollX
                smoothScrollBy(dx, 0)
                velocityTracker.clear()
            }
            else -> {
            }
        }
        lastX = x
        lastY = y

        return true
    }

    private fun smoothScrollBy(dx: Int, dy: Int) {
        scroller.startScroll(scrollX, 0, dx, 0, 500)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    override fun onDetachedFromWindow() {
        velocityTracker.recycle()
        super.onDetachedFromWindow()
    }
}