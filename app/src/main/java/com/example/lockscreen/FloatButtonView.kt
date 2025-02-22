package com.example.lockscreen

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView

class FloatButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.float_button_layout, this)
    }

    private var initialTouchX: Float = 0f
    private var initialTouchY: Float = 0f
    // 存储全局坐标
    private var currentX: Int = 0
    private var currentY: Int = 0
    private var isDragging = false
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var buttonWidth: Int = 0
    private var buttonHeight: Int = 0
    private var onPositionChangedListener: ((Int, Int) -> Unit)? = null

    init {
        post {
            buttonWidth = width
            buttonHeight = height
            updateScreenSize()
        }
    }

    fun setInitialPosition(x: Int, y: Int) {
        currentX = x
        currentY = y
    }

    private fun updateScreenSize() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = wm.currentWindowMetrics
        screenWidth = metrics.bounds.width()
        screenHeight = metrics.bounds.height()
    }

    fun setOnPositionChangedListener(listener: (Int, Int) -> Unit) {
        onPositionChangedListener = listener
    }

    override fun performClick(): Boolean {
        if (!isDragging) {
            super.performClick()
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDragging = false
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = (event.rawX - initialTouchX).toInt()
                val deltaY = (event.rawY - initialTouchY).toInt()
                if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                    isDragging = true
                }
                
                // 计算新位置
                var newX = currentX + deltaX
                var newY = currentY + deltaY
                
                // 边界检查
                newX = newX.coerceIn(0, screenWidth - buttonWidth)
                newY = newY.coerceIn(0, screenHeight - buttonHeight)
                
                // 更新当前位置
                currentX = newX
                currentY = newY
                
                onPositionChangedListener?.invoke(newX, newY)
                
                // 更新初始触摸位置，使移动更流畅
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (!isDragging && Math.abs(event.rawX - initialTouchX) < 10 &&
                    Math.abs(event.rawY - initialTouchY) < 10) {
                    performClick()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}