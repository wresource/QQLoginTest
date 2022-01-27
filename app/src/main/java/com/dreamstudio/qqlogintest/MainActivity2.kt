package com.dreamstudio.qqlogintest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tencent.mmkv.MMKV

class MainActivity2 : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val a:Button = findViewById(R.id.logout)
        a.setOnClickListener {
            QQLoginTestApplication.mTencent.logout(this)
            val  kv = MMKV.defaultMMKV()
            kv.remove("qq_login")
            val intent = Intent(this@MainActivity2,MainActivity::class.java)
            startActivity(intent)
            "退出登录成功".showToast()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCollector.finishAll()
    }
}