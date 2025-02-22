# 将遮罩移除操作改为双击触发

## 当前行为
- 用户单击遮罩时，遮罩会被移除
- 实现在 LockScreenService.kt 的 setupOverlay() 方法中

## 修改计划

### 1. 修改 LockScreenService.kt
- 添加 GestureDetector 成员变量
- 创建 GestureDetector.SimpleOnGestureListener 子类来处理双击事件
- 在 setupOverlay() 中：
  - 初始化 GestureDetector
  - 替换 setOnClickListener 为 setOnTouchListener
  - 在 onTouch 事件中使用 GestureDetector 处理双击

### 代码修改示例
```kotlin
// 添加成员变量
private lateinit var gestureDetector: GestureDetector

// 在 setupOverlay() 中
private fun setupOverlay() {
    // 现有窗口参数设置保持不变...
    
    // 初始化手势检测器
    gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            stopSelf()
            return true
        }
    })

    // 替换点击监听器为触摸监听器
    overlayView?.setOnTouchListener { v, event ->
        gestureDetector.onTouchEvent(event)
        true
    }
}
```

## 优势
1. 更安全的操作：双击比单击更不容易误触
2. 符合用户体验：双击是常见的解锁手势
3. 实现简单：使用 Android 内置的 GestureDetector 可靠且高效

## 测试计划
1. 验证双击可以正确移除遮罩
2. 验证单击不会触发移除
3. 确认时间显示等其他功能正常工作

## 后续建议
- 考虑添加视觉或触觉反馈，在双击时提供用户反馈
- 可以考虑添加设置选项，允许用户选择移除遮罩的方式（单击/双击）