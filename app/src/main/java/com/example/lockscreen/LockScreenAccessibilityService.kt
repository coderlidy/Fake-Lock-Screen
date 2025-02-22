package com.example.lockscreen

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent

class LockScreenAccessibilityService : AccessibilityService() {
    private var windowManager: WindowManager? = null
    private var floatButton: FloatButtonView? = null
    private var overlayView: View? = null
    private var isOverlayShowing = false
    
    // 用于记录悬浮按钮的位置
    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0f
    private var initialTouchY: Float = 0f

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        showFloatButton()
    }

    private fun showFloatButton() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100

        floatButton = FloatButtonView(this).apply {
            setOnClickListener {
                if (!isOverlayShowing) {
                    showOverlay()
                }
            }
            setOnPositionChangedListener { deltaX, deltaY ->
                params.x = (params.x + deltaX).toInt()
                params.y = (params.y + deltaY).toInt()
                windowManager?.updateViewLayout(this, params)
            }
        }

        try {
            windowManager?.addView(floatButton, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showOverlay() {
        if (isOverlayShowing) return
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP

        overlayView = View(this).apply {
            setBackgroundColor(0xFF000000.toInt())
            isClickable = false
            isFocusable = false
        }

        try {
            windowManager?.addView(overlayView, params)
            isOverlayShowing = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideOverlay() {
        if (!isOverlayShowing) return
        
        try {
            overlayView?.let { view ->
                windowManager?.removeView(view)
                isOverlayShowing = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 不需要处理无障碍事件
    }

    override fun onInterrupt() {
        // 不再取消遮罩
    }

    override fun onDestroy() {
        super.onDestroy()
        // 只移除浮动按钮，不取消遮罩
        floatButton?.let { windowManager?.removeView(it) }
    }
}