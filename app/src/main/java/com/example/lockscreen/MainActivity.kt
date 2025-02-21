package com.example.lockscreen

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lockscreen.ui.theme.LockscreenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LockscreenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LockScreenContent()
                }
            }
        }
    }
}

@Composable
fun LockScreenContent() {
    val context = LocalContext.current
    var isAccessibilityEnabled by remember { 
        mutableStateOf(isAccessibilityServiceEnabled(context)) 
    }

    // 监听服务状态变化
    DisposableEffect(Unit) {
        val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val callback = AccessibilityManager.AccessibilityStateChangeListener { enabled ->
            isAccessibilityEnabled = enabled
        }
        accessibilityManager.addAccessibilityStateChangeListener(callback)
        
        onDispose {
            accessibilityManager.removeAccessibilityStateChangeListener(callback)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!isAccessibilityEnabled) {
            Text("需要开启无障碍服务才能使用锁屏功能")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intent)
                },
                modifier = Modifier.width(200.dp)
            ) {
                Text("开启无障碍服务")
            }
        } else {
            Text("无障碍服务已启用，锁屏功能已激活")
            Text("点击屏幕任意位置可关闭锁屏", 
                modifier = Modifier.padding(top = 8.dp))
        }
    }
}

private fun isAccessibilityServiceEnabled(context: Context): Boolean {
    val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val installedServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
    return installedServices.any { it.resolveInfo.serviceInfo.packageName == context.packageName }
}