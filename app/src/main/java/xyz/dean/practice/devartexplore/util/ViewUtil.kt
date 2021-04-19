package xyz.dean.practice.devartexplore.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.IntDef

@IntDef(value = [Toast.LENGTH_SHORT, Toast.LENGTH_LONG])
@Retention(AnnotationRetention.SOURCE)
annotation class Duration

fun Context.toast(msg: String, @Duration duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}