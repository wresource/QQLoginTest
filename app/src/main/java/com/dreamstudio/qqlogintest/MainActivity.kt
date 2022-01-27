package com.dreamstudio.qqlogintest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dreamstudio.qqlogintest.databinding.ActivityMainBinding
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import android.content.Intent
import android.util.Log

/**
 * 这边的登录可能出现异常情况，重新登录即可
 */
class MainActivity :BaseActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var  iu:BaseUiListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        iu = BaseUiListener(QQLoginTestApplication.mTencent)
        setContentView(viewBinding.root)
        viewBinding.Login.setOnClickListener {
             if (!QQLoginTestApplication.mTencent.isSessionValid) {
                when (QQLoginTestApplication.mTencent.login(this, "all",iu)) {
                    0 -> "正常登录".showToast()
                    1 -> "开始登录".showToast()
                    -1 -> {
                        "异常".showToast()
                        QQLoginTestApplication.mTencent.logout(QQLoginTestApplication.context)
                    }
                    2 -> "使用H5登陆或显示下载页面".showToast()
                    else -> "出错".showToast()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCollector.finishAll()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //腾讯QQ回调
        Tencent.onActivityResultData(requestCode, resultCode, data,iu)
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, iu)
            }
        }
    }
}