package cn.nightrainmilkyway.tritium

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.content.Intent
import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException

@SuppressLint("SdCardPath")
class FastTileService : TileService() {

    private val tileUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateTile()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
        val filter = IntentFilter("cn.nightrainmilkyway.tritium.UPDATE_TILE")
        registerReceiver(tileUpdateReceiver, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onStopListening() {
        super.onStopListening()
        unregisterReceiver(tileUpdateReceiver)
    }

    override fun onClick() {
        super.onClick()
        Log.d("FastTileService", "Tile clicked")
        setMode("fast")
        updateTile()
        sendBroadcast(Intent("cn.nightrainmilkyway.tritium.UPDATE_TILE").setPackage(packageName))
    }

    private fun updateTile() {
        val tile = qsTile
        val currentMode = readModeFromFile()
        tile.state = if (currentMode == "fast") Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = "极速模式"
        tile.updateTile()
        Log.d("FastTileService", "Tile updated: $currentMode")
    }

    private fun setMode(mode: String) {
        writeModeToFile(mode)
    }

    private fun writeModeToFile(mode: String) {
        try {
            val filePath = "/data/data/cn.nightrainmilkyway.tritium/files/binaries/mode.txt"
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $mode > $filePath"))
            process.waitFor()
            Log.d("FastTileService", "Mode written to file: $mode")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("FastTileService", "IOException: ${e.message}")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Log.e("FastTileService", "InterruptedException: ${e.message}")
        }
    }

    private fun readModeFromFile(): String {
        return try {
            val filePath = "/data/data/cn.nightrainmilkyway.tritium/files/binaries/mode.txt"
            val file = File(filePath)
            if (file.exists()) {
                val mode = file.readText().trim()
                Log.d("FastTileService", "Mode read from file: $mode")
                mode
            } else {
                "balance"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("FastTileService", "IOException: ${e.message}")
            "balance"
        }
    }
}