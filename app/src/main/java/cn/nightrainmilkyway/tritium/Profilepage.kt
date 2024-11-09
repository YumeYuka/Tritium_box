package cn.nightrainmilkyway.tritium

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import cn.nightrainmilkyway.tritium.ui.theme.TritiumTheme


@Composable
fun ProfileText(url: String) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(140.dp))

        Image(
            painter = painterResource(id = R.mipmap.logo),
            contentDescription = "Profile picture",
            modifier = Modifier
//                .size(270.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
        )
        Spacer(modifier = Modifier.height(360.dp))
        Text("Apache-2.0 license", fontSize = 15.sp, style = MaterialTheme.typography.bodyMedium)
    }
}


@Composable
fun Profile(
    name: String,
    description: String,
    imageResId: Int,
    url: String
) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProfileList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(2f))
        Profile(
            name = "Chenzyadb",
            description = "@ 千人之诺诺, 不如一士之谔谔",
            imageResId = R.mipmap.chenzyadb,
            url = "https://github.com/Chenzyadb"
        )
        Profile(
            name = "NightRainMilkyWay",
            description = "@ 梦——それは現実の続き；現実——それは夢の終わり",
            imageResId = R.mipmap.nightrain,
            url = "https://github.com/NightRainMilkyWay"
        )
        Profile(
            name = "Suni",
            description = "@ 与其仰望星空，不如去做摘星星的人。",
            imageResId = R.mipmap.suni,
            url = "https://github.com/Suni-code"
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun ProfilePreview() {
    TritiumTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.mipmap.home),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        ProfileText("https://github.com/TimeBreeze/Tritium")
        ProfileList()
    }
}
