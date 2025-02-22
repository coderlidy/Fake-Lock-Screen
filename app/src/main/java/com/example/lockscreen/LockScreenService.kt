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
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_SECURE or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP

        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null).apply {
            isClickable = false
            isFocusable = false
            isLongClickable = false
            isFocusableInTouchMode = false
            isEnabled = false  // 禁用所有交互
        }
        windowManager?.addView(overlayView, params)
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}