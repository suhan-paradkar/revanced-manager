package app.revanced.manager

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.revanced.manager.backend.utils.Aapt2
import app.revanced.manager.ui.screens.MainScreen
import app.revanced.manager.ui.theme.ReVancedManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application
        aaptPath = Aapt2.binary(applicationContext).absolutePath
        setContent {
            ReVancedManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    companion object {
        lateinit var app: Application
            private set
        lateinit var aaptPath: String
            private set
    }
}

@Preview
@Composable
fun FullPreview() {
    ReVancedManagerTheme {
        MainScreen()
    }
}