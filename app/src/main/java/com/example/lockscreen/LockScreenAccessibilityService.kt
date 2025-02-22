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
    
    // 用于记录点击相关数据
    private var clickCount = 0
    private var lastClickTime = 0L
    private val CLICK_TIMEOUT = 500L // 点击超时时间（毫秒）
    
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
        
        // 获取真实屏幕高度
        val screenHeight = getRealScreenHeight()
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            screenHeight + getSystemWindowInsetTop() + getNavigationBarHeight(),
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.LEFT
        params.y = -getSystemWindowInsetTop()

        overlayView = View(this).apply {
            setBackgroundColor(0xFF000000.toInt())
            isClickable = true
            isFocusable = false
            
            setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastClickTime > CLICK_TIMEOUT) {
                            // 如果距离上次点击超时，重置点击计数
                            clickCount = 1
                        } else {
                            // 增加点击计数
                            clickCount++
                        }
                        lastClickTime = currentTime
                        
                        // 检查是否达到三击
                        if (clickCount >= 3) {
                            hideOverlay()
                            clickCount = 0
                        }
                        true
                    }
                    else -> true
                }
            }
        }

        try {
            windowManager?.addView(overlayView, params)
            isOverlayShowing = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSystemWindowInsetTop(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    private fun getNavigationBarHeight(): Int {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    private fun getRealScreenHeight(): Int {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val metrics = windowManager?.currentWindowMetrics
        return metrics?.bounds?.height() ?: 0
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