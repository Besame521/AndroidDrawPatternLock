package cn.zzuzx.drawpattern

import android.app.Application

class MyApplication:Application() {

    companion object {
        private var LocalApp:Application? = null
        val MyApp:Application
            get() {
                LocalApp?.let {
                    return it
                } ?: run{
                    throw Exception("MyApp has not init")
                }
            }

    }

    override fun onCreate() {
        super.onCreate()
        LocalApp = this
    }
}