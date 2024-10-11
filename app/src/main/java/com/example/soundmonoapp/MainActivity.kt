package com.example.soundmonoapp

import AudioMonitorService
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundmonoapp.ui.theme.SoundMonoAppTheme
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val sampleRate = 44100
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT)

    // Verifica si el permiso ya ha sido concedido
    private fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }
    // Pide el permiso si no ha sido concedido
    private fun requestAudioPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
    }

    @SuppressLint("MissingPermission")
    private val audioRecorder = AudioRecord(MediaRecorder.AudioSource.MIC,
        sampleRate, AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT, bufferSize)

    private val audioPlayer = AudioTrack(
        AudioTrack.MODE_STREAM, sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT, bufferSize,
        AudioTrack.MODE_STREAM)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkAudioPermission()) {
            setContent { MainScreen() }
        } else {
            requestAudioPermission()
        }

        // Iniciar el servicio
        val serviceIntent = Intent(this, AudioMonitorService::class.java)
        startForegroundService(serviceIntent)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setContent { MainScreen() }
        } else {
            // Permiso denegado, puedes manejarlo aquí
        }
    }

    // Asegúrate de declarar isRecording como una variable de clase
    private var isRecording = false


   /* @Composable
    fun MainScreen() {
        var isRecordingState by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Audio Monitor", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                isRecordingState = !isRecordingState
                isRecording = isRecordingState // Actualiza la variable de clase
                if (isRecordingState) {
                    startAudioMonitoring(coroutineScope)
                } else {
                    stopAudioMonitoring()
                }
            }) {
                Text(if (isRecordingState) "Stop Monitoring" else "Start Monitoring")
            }
        }
    }*/

    @Composable
    fun MainScreen() {
        var isRecordingState by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Audio Monitor",
                fontSize = 30.sp,
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                isRecordingState = !isRecordingState
                isRecording = isRecordingState // Actualiza la variable de clase
                if (isRecordingState) {
                    startAudioMonitoring(coroutineScope) // Pasa el CoroutineScope como parámetro
                } else {
                    stopAudioMonitoring()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ear_listening),
                    contentDescription = "Listen Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isRecordingState) "Stop Monitoring" else "Start Monitoring")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Visit our GitHub",
                color = Color.Gray,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/gcapuccia")
                    )
                    context.startActivity(intent)
                }
            )
        }
    }




        private fun startAudioMonitoring(coroutineScope: CoroutineScope) {
        audioRecorder.startRecording()
        audioPlayer.play()
        val audioBuffer = ShortArray(bufferSize)
        coroutineScope.launch(Dispatchers.Default) {
            while (isRecording) {
                val readBytes = audioRecorder.read(audioBuffer, 0, bufferSize)
                if (readBytes > 0) {
                    audioPlayer.write(audioBuffer, 0, readBytes)
                }
            }
        }
    }

    private fun stopAudioMonitoring() {
        audioRecorder.stop()
        audioPlayer.stop()
    }
}

