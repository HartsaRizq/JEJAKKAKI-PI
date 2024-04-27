package com.folu.jejakkaki


import android.app.Application
import com.yariksoffice.lingver.Lingver


class LanguageApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this, "en")
    }
}