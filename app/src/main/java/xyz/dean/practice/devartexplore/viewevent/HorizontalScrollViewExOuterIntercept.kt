package xyz.dean.practice.devartexplore.viewevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class HorizontalScrollViewExOuterIntercept @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {
    private var childrenSize: Int = 0
    private var childrenWidth: Int = 0
    private var childrenIndex: Int = 0

    private var lastX: Int = 0
    private var lastY: Int = 0

    private var lastXIntercept: Int = 0
    private var lastYIntercept: Int = 0

    private val scroller = Scroller(context)
    private val velocityTracker = VelocityTracker.obtain()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measureWith = 0
        var measureHeight = 0
        val childCount = childCount

        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)

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
        var intercepted = false
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 父容器不拦截Down事件，以保证事件可以传递到子View
                intercepted = false
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                    intercepted = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // 在Move事件中根据情况判断是否要拦截
                // 通常来说，如果父View决定不拦截，则后续的事件还会继续调用onInterceptTouchEvent以询问父view是否需要拦截
                //     一旦父view决定拦截某一个事件，则后续事件都将交由父view处理
                // 需要注意的是，在个别子view中，如ListView，会在某些时候主动调用requestDisallowDispatchTouchEvent方法来禁止父View
                // 拦截后续的事件，从而表现出：几个Move事件过后，父view的onInterceptTouchEvent将不再调用。
                val deltaX = x - lastXIntercept
                val deltaY = y - lastYIntercept
                intercepted = abs(deltaX) > abs(deltaY)
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
            else -> {
            }
        }
        lastX = x
        lastY = y
        lastXIntercept = x
        lastYIntercept = y

        return intercepted
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