package com.example.lockscreen

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
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
    private var onPositionChangedListener: ((Float, Float) -> Unit)? = null
    private var isDragging = false

    fun setOnPositionChangedListener(listener: (Float, Float) -> Unit) {
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
                val deltaX = event.rawX - initialTouchX
                val deltaY = event.rawY - initialTouchY
                if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                    isDragging = true
                }
                onPositionChangedListener?.invoke(deltaX, deltaY)
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