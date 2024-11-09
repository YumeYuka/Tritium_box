package cn.nightrainmilkyway.tritium

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException

@Composable
fun IconTitleBox(
    icon: Int,
    title: String,
    description: String,
    backgroundColor: Color,
    iconSize: Int = 24,
    cornerRadius: Int = 20,
    modifier: Modifier = Modifier,
    showSwitch: Boolean = false,
    initialSwitchChecked: Boolean = false,
    onSwitchChanged: (Boolean) -> Unit = {},
    iconBackgroundColor: Color = Color(0xFFf7e3e1),
    url: String? = null,
    preferenceKey: String,
    scriptPath: String? = null
) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val scope = rememberCoroutineScope()
    val switchState = remember { mutableStateOf(
        sharedPreferences.getBoolean(preferenceKey, initialSwitchChecked)
    ) }
    if (switchState.value) {
        if (isRooted()) {
            scriptPath?.let {
                setFilePermissionsAndRunScript(it)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(backgroundColor, RoundedCornerShape(cornerRadius.dp))
            .padding(10.dp)
            .clickable(enabled = url != null) {
                url?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    context.startActivity(intent)
                }
            }
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size((iconSize + 16).dp)
                    .background(iconBackgroundColor, RoundedCornerShape(16.dp))
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier
                        .size(iconSize.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = TextStyle(color = Color.Black, fontSize = MaterialTheme.typography.titleMedium.fontSize),
                )
                Text(
                    text = description,
                    style = TextStyle(color = Color.Black.copy(alpha = 0.7f), fontSize = MaterialTheme.typography.bodySmall.fontSize)
                )
            }
        }
        if (showSwitch) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = switchState.value,
                    onCheckedChange = { newChecked ->
                        switchState.value = newChecked
                        sharedPreferences.edit().putBoolean(preferenceKey, newChecked).apply()
                        onSwitchChanged(newChecked)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color(0xFF904a43),
                        checkedTrackColor = Color(0xFF8F4942),
                        uncheckedTrackColor = Color(0xFFFBF4F3)
                    )
                )
            }
        }
    }
}

fun isRooted(): Boolean {
    return try {
        val process = Runtime.getRuntime().exec("which su")
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val result = reader.readLine()
        reader.close()
        result != null && result.isNotEmpty()
    } catch (e: Exception) {
        false
    }
}

fun setFilePermissionsAndRunScript(scriptPath: String) {
    try {
        val process = Runtime.getRuntime().exec("su")
        val outputStream = process.outputStream

        outputStream.write("chmod 777 $scriptPath\n".toByteArray())
        outputStream.flush()

        outputStream.write("$scriptPath\n".toByteArray())
        outputStream.flush()

        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@SuppressLint("SdCardPath")
@Preview
@Composable
fun IconTitleBoxPreview() {
    IconTitleBox(
        icon = R.drawable.debug,
        title = "版本号",
        description = "1.0.0_Beta",
        backgroundColor = Color(0xFFfdf2f0),
        showSwitch = true,
        initialSwitchChecked = false,
        onSwitchChanged = { newChecked -> println("Switch is now: $newChecked") },
        iconBackgroundColor = Color(0xFFf7e3e1),
        preferenceKey = "settings_switch_state",
        scriptPath = "/data/data/com.example.app/files/modules/myscript.sh"
    )
}
