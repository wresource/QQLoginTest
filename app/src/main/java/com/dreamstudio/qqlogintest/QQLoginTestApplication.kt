package com.dreamstudio.qqlogintest

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import com.tencent.tauth.Tencent

class QQLoginTestApplication: Application()  {
    companion object{
        /**
         * 用于全局获取上下文
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var  mTencent:Tencent


    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        MMKV.initialize(context)
        mTencent = Tencent.createInstance("你的id", context, "com.tencent.login.fileprovider")
    }
}