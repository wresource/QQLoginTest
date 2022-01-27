package com.dreamstudio.qqlogintest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Path
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.gson.Gson
import com.tencent.connect.common.Constants
import com.tencent.mmkv.MMKV
import com.tencent.tauth.DefaultUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject

class BootApplication : BaseActivity() {
    private val kv = MMKV.defaultMMKV()
    private lateinit var  iu:BaseUiListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //启动动画
        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_boot_application)

        //初始化配置
        Tencent.setIsPermissionGranted(true, Build.MODEL)
        Tencent.resetTargetAppInfoCache()
        Tencent.resetQQAppInfoCache()
        Tencent.resetTimAppInfoCache()
        iu = BaseUiListener(QQLoginTestApplication.mTencent)
        kv.decodeString("qq_login")?.let{
            val gson = Gson()
            val qqLogin = gson.fromJson(it, QQLogin::class.java)
            QQLoginTestApplication.mTencent.setAccessToken(qqLogin.access_token,qqLogin.expires_in.toString())
            QQLoginTestApplication.mTencent.openId = qqLogin.openid
        }
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            showSplashIconExitAnimator(splashScreenViewProvider.iconView) {
                splashScreenViewProvider.remove()
            }
        }
        QQLoginTestApplication.mTencent.checkLogin(object : DefaultUiListener() {
            override fun onComplete(response: Any) {
                val jsonResp = response as JSONObject

                if (jsonResp.optInt("ret", -1) == 0) {
                    val jsonObject: String? = kv.decodeString("qq_login")
                    if (jsonObject == null) {
                        "登录失败".showToast()

                    } else {

                        val intent = Intent(this@BootApplication,MainActivity2::class.java)
                        startActivity(intent)
                    }
                } else {
                    "登录已过期，请重新登录".showToast()

                    val intent = Intent(this@BootApplication,MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onError(e: UiError) {
                "登录已过期，请重新登录".showToast()
                val intent = Intent(this@BootApplication,MainActivity::class.java)
                startActivity(intent)
            }

            override fun onCancel() {
                "取消登录".showToast()
            }
        })
    }
    //这个回调改不了，只能等腾讯api更新了再改
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

    private fun showSplashIconExitAnimator(splashScreenView: View, onExit: () -> Unit = {}) {
        Log.d("Splash", "showSplashExitAnimator() splashScreenView:$splashScreenView" +
                " context:${splashScreenView.context}" +
                " parent:${splashScreenView.parent}")

        // Create your custom animation set.
        val slideUp = ObjectAnimator.ofFloat(
            splashScreenView,
            View.TRANSLATION_Y,
            0f,
            -splashScreenView.height.toFloat()
        )

        val slideLeft = ObjectAnimator.ofFloat(
            splashScreenView,
            View.TRANSLATION_X,
            0f,
            -splashScreenView.width.toFloat()
        )

        val scaleXOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.SCALE_X,
            1.0f,
            0f
        )

        val alphaOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.ALPHA,
            1f,
            0f
        )

        val scaleOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0f, 0f)
            }
        )

        AnimatorSet().run {
            val defaultExitDuration = 1000
            duration = defaultExitDuration.toLong()
            interpolator = AnticipateInterpolator()
            Log.d("Splash", "showSplashExitAnimator() duration:$duration")

//             playTogether(alphaOut)
            playTogether(scaleOut,alphaOut)

            doOnEnd {
                Log.d("Splash", "showSplashExitAnimator() onEnd")
                // Log.d("Splash", "showSplashExitAnimator() onEnd remove")
                onExit()
            }
            start()
        }
    }
}