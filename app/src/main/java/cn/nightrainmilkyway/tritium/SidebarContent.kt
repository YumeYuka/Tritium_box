package cn.nightrainmilkyway.tritium

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

@SuppressLint("SdCardPath")
@Composable
fun SidebarContent() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(
                color = Color(0xFFfff8f7),
                RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "设置",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.Black),
                modifier = Modifier
                    .padding(start = 16.dp, top = 32.dp)
                    .padding(bottom = 16.dp)
            )
            IconTitleBox(
                icon = R.drawable.settings_24dp_5f6368,
                title = "保活",
                description = "使app服务存活",
                backgroundColor = Color(0xFFfdf2f0),
                showSwitch = true,
                initialSwitchChecked = false,
                onSwitchChanged = { newChecked ->
                    println("Switch is now: $newChecked")
                },
                iconBackgroundColor = Color(0xFFf7e3e1),
                preferenceKey = "setting",
                scriptPath = "/data/data/cn.nightrainmilkyway.tritium/files/scripts/keep_alive.sh"
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconTitleBox(
                icon = R.drawable.view_module_24dp_5f6368,
                title = "附加模块",
                description = "将附加模块输出到DownLoad",
                backgroundColor = Color(0xFFfdf2f0),
                showSwitch = true,
                initialSwitchChecked = false,
                onSwitchChanged = { newChecked ->
                    println("Switch is now: $newChecked")
                },
                iconBackgroundColor = Color(0xFFf7e3e1),
                preferenceKey = "setting2",
                scriptPath = "/data/data/cn.nightrainmilkyway.tritium/files/scripts/copy_modules.sh"
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconTitleBox(
                icon = R.drawable.verified_24dp_5f6368,
                title = "版本号",
                description = "1.0.0_Beta",
                backgroundColor = Color(0xFFfdf2f0),
                preferenceKey = "settings_switch_state"
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconTitleBox(
                icon = R.drawable.github,
                title = "源代码",
                description = "获取最新更新，讨论问题等",
                backgroundColor = Color(0xFFfdf2f0),
                url = "https://github.com/TimeBreeze/Tritium_box",
                preferenceKey = "settings_switch_state"
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconTitleBox(
                icon = R.drawable.help_center_24dp_5f6368,
                title = "文档",
                description = "阅读文档解决问题",
                backgroundColor = Color(0xFFfdf2f0),
                url = "https://tritium.nightrainmilkyway.cn",
                preferenceKey = "settings_switch_state"
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconTitleBox(
                icon = R.drawable.error_outline_24dp_5f6368,
                title = "错误",
                description = "请使用GitHub提issues",
                backgroundColor = Color(0xFFfdf2f0),
                url = "https://github.com/TimeBreeze/Tritium/issues/new?assignees=&labels=&projects=&template=bug-%E5%8F%8D%E9%A6%88.md&title=",
                preferenceKey = "settings_switch_state"
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconTitleBox(
                icon = R.drawable.group_add_24dp_5f6368,
                title = "群组",
                description = "交流探讨",
                backgroundColor = Color(0xFFfdf2f0),
                url = "https://qm.qq.com/q/8Vsq7Cn224",
                preferenceKey = "settings_switch_state"
            )
        }
    }
}


@Preview
@Composable
fun SidebarPreview() {
    SidebarContent()
}
