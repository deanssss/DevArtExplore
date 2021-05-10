package xyz.dean.practice.devartexplore.util

import android.content.Context
import android.view.MotionEvent
import android.view.View.MeasureSpec
import android.widget.Toast
import androidx.annotation.IntDef

@IntDef(value = [Toast.LENGTH_SHORT, Toast.LENGTH_LONG])
@Retention(AnnotationRetention.SOURCE)
annotation class Duration

fun Context.toast(msg: String, @Duration duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

operator fun MotionEvent.component1() = x.toInt()
operator fun MotionEvent.component2() = y.toInt()
operator fun MotionEvent.component3() = action

operator fun Int.component1() = MeasureSpec.getMode(this)
operator fun Int.component2() = MeasureSpec.getSize(this)