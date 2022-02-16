package cn.zzuzx.drawpattern

import android.content.Context

object AppInfo {
    private val sp = MyApplication.MyApp.getSharedPreferences("app_cache", Context.MODE_PRIVATE)

    fun getGesturePattern(): String{
        return sp.getString("GesturePattern", "").orEmpty()
    }

    fun putGesturePattern(value: String){
        sp.edit().putString("GesturePattern", value).apply()
    }

    fun clear(){
        sp.edit().clear().apply()
    }
}