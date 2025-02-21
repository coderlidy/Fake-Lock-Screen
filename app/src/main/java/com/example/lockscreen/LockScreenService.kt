package com.example.lockscreen

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class LockScreenService : Service() {
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var timeUpdateTimer: Timer? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        setupOverlay()
        startTimeUpdates()
    }

    private fun setupOverlay() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP

        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)
        windowManager?.addView(overlayView, params)

        // 设置点击事件来关闭悬浮窗
        overlayView?.setOnClickListener {
            stopSelf()
        }
    }

    private fun startTimeUpdates() {
        timeUpdateTimer = Timer()
        timeUpdateTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val currentTime = timeFormat.format(Date())
                overlayView?.findViewById<TextView>(R.id.timeText)?.post {
                    overlayView?.findViewById<TextView>(R.id.timeText)?.text = currentTime
                }
            }
        }, 0, 1000) // 每秒更新一次
    }

    override fun onDestroy() {
        timeUpdateTimer?.cancel()
        if (overlayView != null && windowManager != null) {
            windowManager?.removeView(overlayView)
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}