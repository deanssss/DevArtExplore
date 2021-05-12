package xyz.dean.practice.devartexplore.viewevent

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import xyz.dean.practice.devartexplore.util.component1
import xyz.dean.practice.devartexplore.util.component2
import xyz.dean.practice.devartexplore.util.component3
import kotlin.math.abs

class StickyLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {
    private lateinit var mHeader: View
    private lateinit var mContent: View

    private var mOriginalHeaderHeight = 0
    private var mHeaderHeight = 0
    private var mStatus = STATUS_EXPANDED
    private var mTouchSlop = 0

    private var mLastX = 0
    private var mLastY = 0
    private var lastInterceptX = 0
    private var lastInterceptY = 0

    private var mIsSticky = true
    private var mInitDataSucceed = false

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus && (!::mHeader.isInitialized || !::mContent.isInitialized)) {
            initData()
        }
    }

    private fun initData() {
        val headerId = resources.getIdentifier("sticky_header", "id", context.packageName)
        val contentId = resources.getIdentifier("sticky_content", "id", context.packageName)
        if (headerId != 0 && contentId != 0) {
            mHeader = findViewById(headerId)
            mContent = findViewById(contentId)
            mOriginalHeaderHeight = mHeader.measuredHeight
            mHeaderHeight = mOriginalHeaderHeight
            mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
            if (mHeaderHeight > 0) {
                mInitDataSucceed = true
            }
            if (DEBUG) {
                Log.d(TAG, "mTouchSlop = " + mTouchSlop + "mHeaderHeight = " + mHeaderHeight)
            }
        } else {
            throw NoSuchElementException("Did your view with id \"sticky_header\" or \"sticky_content\" exists?")
        }
    }

    var onContentGiveUpEvent: ((Int) -> Boolean) = { false }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var intercepted = false
        val (x, y, c) = event
        when (c) {
            MotionEvent.ACTION_DOWN -> {
                lastInterceptX = x
                lastInterceptY = y
                mLastX = x
                mLastY = y
                intercepted = false
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastInterceptX
                val deltaY = y - lastInterceptY
                if (abs(deltaY) <= abs(deltaX)) {
                    // 只处理竖直方向的滑动，不拦截水平方向的滑动。
                    intercepted = false
                } else if (mStatus == STATUS_EXPANDED && abs(deltaY) >= mTouchSlop) {
                    // header展开状态，拦截所有事件。
                    intercepted = true
                } else if (mStatus == STATUS_COLLAPSED && deltaY >= mTouchSlop) {
                    // header关闭状态，根据content状态拦截
                    if (onContentGiveUpEvent(deltaY)) {
                        intercepted = true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
                lastInterceptY = 0
                lastInterceptX = 0
            }
            else -> {
            }
        }
        if (DEBUG) {
            Log.d(TAG, "intercepted=$intercepted")
        }
        return intercepted && mIsSticky
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mIsSticky) return true

        val (x, y, c) = event
        when (c) {
            MotionEvent.ACTION_MOVE -> headerHeight += (y - mLastY)
            MotionEvent.ACTION_UP -> {
                val destHeight = if (mHeaderHeight <= mOriginalHeaderHeight * 0.5) 0 else mOriginalHeaderHeight
                smoothSetHeaderHeight(mHeaderHeight, destHeight, 500)
            }
            else -> { }
        }
        mLastX = x
        mLastY = y
        return true
    }

    private fun smoothSetHeaderHeight(from: Int, to: Int, duration: Long) {
        val frameCount = (duration / 1000f * 30).toInt() + 1
        val partation = (to - from) / frameCount.toFloat()
        object : Thread("Thread#smoothSetHeaderHeight") {
            override fun run() {
                for (i in 0 until frameCount) {
                    val height = if (i == frameCount - 1) to
                    else (from + partation * i).toInt()
                    post { headerHeight = height }
                    sleep(10)
                }
            }
        }.start()
    }

    var headerHeight: Int
        get() = mHeaderHeight
        set(height) {
            var h = height
            if (!mInitDataSucceed) {
                initData()
            }
            if (DEBUG) {
                Log.d(TAG, "setHeaderHeight height=$h")
            }

            if (h <= 0) {
                h = 0
            } else if (h > mOriginalHeaderHeight) {
                h = mOriginalHeaderHeight
            }

            mStatus = if (h == 0) STATUS_COLLAPSED else STATUS_EXPANDED
            mHeader.layoutParams.height = h
            mHeader.requestLayout()
            mHeaderHeight = h
        }

    companion object {
        private const val TAG = "StickyLayout"
        private const val DEBUG = true
        const val STATUS_EXPANDED = 1
        const val STATUS_COLLAPSED = 2
    }
}