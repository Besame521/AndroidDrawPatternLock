package cn.zzuzx.drawpattern

import android.content.Context

/**
 * dp转px
 */
fun Float.dp2px(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

val Float.dp2px
    get() = run {
        val scale = MyApplication.MyApp.resources.displayMetrics.density
        this * scale + 0.5f
    }.toInt()

val Int.dp2px
    get() = toFloat().dp2px