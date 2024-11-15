package cn.nightrainmilkyway.tritium

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 处理无障碍事件
    }

    override fun onInterrupt() {
        // 处理中断
    }

    @SuppressLint("ForegroundServiceType")
    override fun onServiceConnected() {
        super.onServiceConnected()
        // 配置服务
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
        }
        serviceInfo = info

        // 启动前台服务
        val notification = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle("无障碍服务")
            .setContentText("服务正在运行")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .build()

        startForeground(1, notification)
    }

    companion object {
        fun startService(context: Context) {
            val intent = Intent(context, MyAccessibilityService::class.java)
            context.startService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, MyAccessibilityService::class.java)
            context.stopService(intent)
        }
    }
}