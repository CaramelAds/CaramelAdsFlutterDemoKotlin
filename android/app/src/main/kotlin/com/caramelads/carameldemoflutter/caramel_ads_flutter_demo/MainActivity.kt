package com.caramelads.carameldemoflutter.caramel_ads_flutter_demo

import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.caramelads.sdk.CaramelAdListener
import com.caramelads.sdk.CaramelAds
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {
    @Override
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "initialize" -> caramelInitialize()
                    "isloaded" -> result.success(caramelIsLoaded())
                    "cache" -> caramelCache()
                }
            }
    }

    fun caramelInitialize() {
        CaramelAds.initialize(this@MainActivity)
        CaramelAds.setAdListener(object : CaramelAdListener {
            @Override
            override fun sdkReady() {
                showToast(
                    "SDK READY",
                    "sdk is ready, wait while ad is load to cache and Caramel button is enable"
                )
                //cache ads after CaramelSDK is ready
                CaramelAds.cache(this@MainActivity)
            }

            @Override
            override fun sdkFailed() {
                showToast("SDK FAILED", "sdk is failed")
            }

            @Override
            override fun adLoaded() {
                showToast("AD LOADED", "ad is loaded and you can push the Caramel button")
            }

            @Override
            override fun adOpened() {
                showToast("AD OPENED", "ad is opened")
            }

            @Override
            override fun adClicked() {
                showToast("AD CLICKED", "clicked on ad")
            }

            @Override
            override fun adClosed() {
                showToast("AD CLOSED", "ad is closed")
            }

            @Override
            override fun adFailed() {
                showToast("AD FAILED", "ad is failed")
            }
        })
    }

    fun caramelIsLoaded(): Boolean {
        if (CaramelAds.isLoaded() === true) {
            CaramelAds.show()
            return true
        } else showToast("WAIT", "wait while ad is load to cache and Caramel button is enable")
        return false
    }

    fun caramelCache() {
        CaramelAds.cache(this@MainActivity)
    }

    private fun showToast(title: String, text: String) {
        val toast = Toast(getApplicationContext())
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0)
        toast.setDuration(Toast.LENGTH_LONG)
        val grad = GradientDrawable()
        grad.setColor(-0x1)
        grad.setCornerRadius(6F)
        grad.setStroke(2, -0x1000000)
        val tv = TextView(this)
        tv.setText(Html.fromHtml("<b><font color=#f89406>$title:<br></font></b><br>$text"))
        tv.setTextSize(20F)
        tv.setGravity(Gravity.CENTER)
        tv.setBackground(grad)
        toast.setView(tv)
        toast.show()
    }

    companion object {
        private const val CHANNEL = "caramel.flutter/native"
    }
}
