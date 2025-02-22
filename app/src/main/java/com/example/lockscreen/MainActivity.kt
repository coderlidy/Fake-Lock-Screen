package com.example.lockscreen

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import kotlinx.coroutines.delay
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

    // 使用LaunchedEffect来定期检查服务状态
    LaunchedEffect(Unit) {
        val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        
        while (true) {
            val enabled = isAccessibilityServiceEnabled(context)
            if (isAccessibilityEnabled != enabled) {
                isAccessibilityEnabled = enabled
            }
            kotlinx.coroutines.delay(1000) // 每100毫秒检查一次
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "无障碍服务已启用，锁屏功能已激活",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text("点击悬浮锁屏按钮开启")
                Text("点击屏幕任意位置5下关闭")
            }
        }
    }
}

private fun isAccessibilityServiceEnabled(context: Context): Boolean {
    val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val installedServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
    return installedServices.any { it.resolveInfo.serviceInfo.packageName == context.packageName }
}