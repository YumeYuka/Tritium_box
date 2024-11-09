package cn.nightrainmilkyway.tritium


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.nightrainmilkyway.tritium.ui.theme.TritiumTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException

@SuppressLint("SdCardPath")
@Composable
fun Log() {
    var fileContent by remember { mutableStateOf("加载中...") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        fileContent = loadFileContentWithRoot("/data/data/cn.nightrainmilkyway.tritium/files/binaries/log.txt")
    }

    TritiumTheme {
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
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.7f)
                    .align(Alignment.Center)
                    .offset(y = 40.dp)
                    .graphicsLayer {
                        alpha = 1f
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = fileContent,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

fun loadFileContentWithRoot(filePath: String): String {
    return try {
        val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "cat $filePath"))
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val result = reader.use { it.readText() }
        process.waitFor()
        result
    } catch (e: IOException) {
        "无法读取文件：${e.localizedMessage}"
    } catch (e: InterruptedException) {
        "读取文件时发生错误：${e.localizedMessage}"
    }
}

@Preview(showBackground = true)
@Composable
fun LogPreview() {
    Log()
}
