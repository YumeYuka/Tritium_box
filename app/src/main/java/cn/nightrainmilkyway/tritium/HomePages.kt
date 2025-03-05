package cn.nightrainmilkyway.tritium

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.nightrainmilkyway.tritium.ui.theme.TritiumTheme
import java.io.IOException
import java.io.File

@Preview
@Composable
fun HomePages() {
    TritiumTheme {
        val context = LocalContext.current
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.mipmap.home),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.atri1),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                        .offset(y = 10.dp),
                )
                var currentMode by remember { mutableStateOf(readModeFromFile()) }

                ModeDisplayBlock(
                    currentMode = currentMode,
                    onModeChange = { newMode ->
                        currentMode = newMode
                        writeModeToFile(newMode)
                        context.sendBroadcast(Intent("cn.nightrainmilkyway.tritium.UPDATE_TILE"))
                    }
                )
            }
        }
    }
}

@Composable
fun ModeDisplayBlock(currentMode: String, onModeChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(16.dp)
            .offset(y = 100.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFfdf2f0)
        ),
        elevation = CardDefaults.elevatedCardElevation(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "当前模式: $currentMode",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val modes = listOf("省电", "均衡", "性能", "极速")

                modes.forEachIndexed { index, mode ->
                    val shape = when (index) {
                        0 -> RoundedCornerShape(16.dp, 0.dp, 0.dp, 16.dp)
                        modes.size - 1 -> RoundedCornerShape(0.dp, 16.dp, 16.dp, 0.dp)
                        else -> RoundedCornerShape(0.dp)
                    }

                    Card(
                        onClick = { onModeChange(mode) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp),
                        shape = shape,
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFfdf2f0)
                        ),
                        elevation = CardDefaults.elevatedCardElevation(6.dp),
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = mode, color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("SdCardPath")
fun writeModeToFile(mode: String) {
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
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

@SuppressLint("SdCardPath")
fun readModeFromFile(): String {
    return try {
        val filePath = "/data/data/cn.nightrainmilkyway.tritium/files/binaries/mode.txt"
        val file = File(filePath)
        if (file.exists()) {
            when (file.readText().trim()) {
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
        "均衡"
    }
}