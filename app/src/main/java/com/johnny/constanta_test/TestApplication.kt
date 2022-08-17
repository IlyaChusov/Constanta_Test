package com.johnny.constanta_test

import android.app.Application
import com.johnny.constanta_test.util.PreferenceWork

class TestApplication : Application() {

    override fun onCreate() {
        PreferenceWork.init(this)
        super.onCreate()
    }
}