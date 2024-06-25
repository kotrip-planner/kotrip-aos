package com.koreatech.kotrip_android

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.utils.Utils
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.koreatech.kotrip_android.di.kotripModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class KotripApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.e("aaa", "${Utility.getKeyHash(this)}")
        KakaoSdk.init(this, BuildConfig.kakao_native_app_key)
        startKoin {
            if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@KotripApplication)
            modules(kotripModule)
        }
    }
}