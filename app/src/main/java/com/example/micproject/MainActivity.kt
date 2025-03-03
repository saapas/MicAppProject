package com.example.micproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.micproject.playback.AndroidAudioPlayer
import com.example.micproject.record.AndroidAudioRecorder
import com.example.micproject.ui.theme.MicProjectTheme
import java.io.File
import android.Manifest
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setOnExitAnimationListener{  screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.7f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.7f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        setContent {
            MicProjectTheme {
                Column (
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Audio Recorder and Player",
                        fontSize = 24.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    IconButton(
                        onClick = {
                            File(cacheDir, "audio.mp3").also {
                                recorder.start(it)
                                audioFile = it
                            }
                        },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Record",
                            tint = Color.Red,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Text(text = "Record", modifier = Modifier.padding(top = 8.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    IconButton(
                        onClick = { recorder.stop() },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_crop_square_24),
                            contentDescription = "Stop Recording",
                            tint = Color.Black,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Text(text = "Stop Recording", modifier = Modifier.padding(top = 8.dp))

                    Spacer(modifier = Modifier.height(32.dp))

                    // Play Button
                    IconButton(
                        onClick = { audioFile?.let { player.playFile(it) } },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_right_24),
                            contentDescription = "Play",
                            tint = Color.Green,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Text(text = "Play", modifier = Modifier.padding(top = 8.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stop Playing Button
                    IconButton(
                        onClick = { player.stop() },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_density_large_24),
                            contentDescription = "Stop Playing",
                            tint = Color.Black,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Text(text = "Stop Playing", modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
