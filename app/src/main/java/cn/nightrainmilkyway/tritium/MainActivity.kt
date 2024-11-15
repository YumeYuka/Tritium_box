package cn.nightrainmilkyway.tritium

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import cn.nightrainmilkyway.tritium.ui.theme.TritiumTheme
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.IOException
import android.content.Context
import android.content.res.AssetManager
import android.view.accessibility.AccessibilityEvent
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    @SuppressLint("SdCardPath")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 开启沉浸式模式

        extractAssetsToInternalStorage(this)
        if (!isRooted()) {
            Toast.makeText(this, "设备未root或为授予root权限.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        setFilePermissionsAndRunScript("/data/data/cn.nightrainmilkyway.tritium/files/scripts/init.sh")

        setContent {
            TritiumTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                    ) {
                        SidebarContent()
                    }
                }, content = {
                    Scaffold(modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            WindowInsets.systemBars
                                .only(WindowInsetsSides.Bottom)
                                .asPaddingValues()
                        ), topBar = {
                        TopAppBar(title = {}, actions = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Settings",
                                    tint = Color.Black
                                )
                            }
                        }, colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = Color.Transparent
                        ), modifier = Modifier.statusBarsPadding()
                        )
                    }, bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable("home") { HomePages() }
                            composable("debug") { Log() }
                            composable("about") { ProfilePreview() }
                        }
                    }
                })
            }
        }
    }

    fun isRooted(): Boolean {
        try {
            val process = Runtime.getRuntime().exec("which su")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val result = reader.readLine()
            reader.close()
            return result != null && result.isNotEmpty()
        } catch (e: Exception) {
            return false
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavController) {
        val selectedItem = remember { mutableStateOf(0) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
                .shadow(4.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
        ) {
            NavigationBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBarItem(icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home"
                    )
                },
                    label = { if (selectedItem.value == 0) Text("主页") else null },
                    selected = selectedItem.value == 0,
                    onClick = {
                        selectedItem.value = 0
                        navController.navigate("home")
                    })
                NavigationBarItem(icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.debug),
                        contentDescription = "Debug"
                    )
                },
                    label = { if (selectedItem.value == 1) Text("调试") else null },
                    selected = selectedItem.value == 1,
                    onClick = {
                        selectedItem.value = 1
                        navController.navigate("debug")
                    })
                NavigationBarItem(icon = {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "About"
                    )
                },
                    label = { if (selectedItem.value == 2) Text("关于") else null },
                    selected = selectedItem.value == 2,
                    onClick = {
                        selectedItem.value = 2
                        navController.navigate("about")
                    })
            }
        }
    }

    fun extractAssetsToInternalStorage(context: Context) {
        try {
            val assetManager = context.assets
            val files = assetManager.list("")
            val targetDir = context.filesDir

            if (!targetDir.exists()) {
                targetDir.mkdir()
            }

            files?.forEach { fileName ->
                val targetFile = File(targetDir, fileName)
                if (isDirectory(assetManager, fileName)) {

                    if (!targetFile.exists()) {
                        targetFile.mkdir()
                    }

                    extractAssetsToInternalStorageForFolder(assetManager, fileName, targetFile)
                } else {
                    if (!targetFile.exists()) {
                        copyAssetToFile(assetManager, fileName, targetFile)
                    }
                }
            }
            Toast.makeText(context, "Assets extracted successfully", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to extract assets", Toast.LENGTH_SHORT).show()
        }
    }

    fun isDirectory(assetManager: AssetManager, assetName: String): Boolean {
        return try {
            val files = assetManager.list(assetName)
            files?.isNotEmpty() == true
        } catch (e: IOException) {
            false
        }
    }

    fun extractAssetsToInternalStorageForFolder(
        assetManager: AssetManager,
        folderName: String,
        targetFolder: File
    ) {
        try {
            val filesInFolder = assetManager.list(folderName)

            filesInFolder?.forEach { fileName ->
                val assetPath = "$folderName/$fileName"
                val targetFile = File(targetFolder, fileName)

                if (isDirectory(assetManager, assetPath)) {
                    if (!targetFile.exists()) {
                        targetFile.mkdir()
                    }
                    extractAssetsToInternalStorageForFolder(assetManager, assetPath, targetFile)
                } else {
                    if (!targetFile.exists()) {
                        copyAssetToFile(assetManager, assetPath, targetFile)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun copyAssetToFile(assetManager: AssetManager, assetName: String, targetFile: File) {
        try {
            val inputStream: InputStream = assetManager.open(assetName)
            val outputStream: OutputStream = FileOutputStream(targetFile)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

