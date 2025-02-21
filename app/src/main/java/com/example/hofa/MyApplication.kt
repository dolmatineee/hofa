package com.example.hofa

import android.app.Application
import com.yandex.mobile.ads.common.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {

        }
    }
}