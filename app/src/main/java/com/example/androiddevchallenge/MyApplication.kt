package com.example.androiddevchallenge

import android.app.Application

class MyApplication : Application() {
    companion object {
        var application: Application? = null
    }


    override fun onCreate() {
        super.onCreate()
        application = this

    }
}