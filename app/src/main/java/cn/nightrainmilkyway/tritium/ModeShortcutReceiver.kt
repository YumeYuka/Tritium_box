package cn.nightrainmilkyway.tritium

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ModeShortcutReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val mode = intent.getStringExtra("mode") ?: return
        writeModeToFile(mode)
    }
}