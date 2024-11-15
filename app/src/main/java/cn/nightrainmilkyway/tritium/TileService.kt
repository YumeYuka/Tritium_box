package cn.nightrainmilkyway.tritium

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.content.Intent
import android.annotation.SuppressLint
import android.util.Log
import java.io.File
import java.io.IOException

@SuppressLint("SdCardPath")
class ModeTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()
        Log.d("ModeTileService", "Tile clicked")
        toggleMode()
        updateTile()
        sendBroadcast(Intent("cn.nightrainmilkyway.tritium.UPDATE_TILE"))
    }

    private fun updateTile() {
        val tile = qsTile
        val currentMode = readModeFromFile()
        tile.state = Tile.STATE_ACTIVE
        tile.label = "当前模式: $currentMode"
        tile.updateTile()
        Log.d("ModeTileService", "Tile updated: $currentMode")
    }

    private fun toggleMode() {
        val currentMode = readModeFromFile()
        val newMode = when (currentMode) {
            "省电" -> "均衡"
            "均衡" -> "性能"
            "性能" -> "极速"
            "极速" -> "省电"
            else -> "均衡"
        }
        writeModeToFile(newMode)
    }

    private fun writeModeToFile(mode: String) {
        try {
            val filePath = "/data/data/cn.nightrainmilkyway.tritium/files/binaries/mode.txt"
            val modeText = when (mode) {
                "省电" -> "powersave"
                "均衡" -> "balance"
                "性能" -> "performance"
                "极速" -> "fast"
                else -> "balance"
            }
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $modeText > $filePath"))
            process.waitFor()
            Log.d("ModeTileService", "Mode written to file: $mode")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ModeTileService", "IOException: ${e.message}")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Log.e("ModeTileService", "InterruptedException: ${e.message}")
        }
    }

    private fun readModeFromFile(): String {
        return try {
            val filePath = "/data/data/cn.nightrainmilkyway.tritium/files/binaries/mode.txt"
            val file = File(filePath)
            if (file.exists()) {
                val mode = file.readText().trim()
                Log.d("ModeTileService", "Mode read from file: $mode")
                when (mode) {
                    "powersave" -> "省电"
                    "balance" -> "均衡"
                    "performance" -> "性能"
                    "fast" -> "极速"
                    else -> "均衡"
                }
            } else {
                "均衡"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ModeTileService", "IOException: ${e.message}")
            "均衡"
        }
    }
}