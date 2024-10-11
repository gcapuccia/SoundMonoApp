package com.example.soundmonoapp

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoundMonoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}*/
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkAudioPermission()) {
            setContent { MainScreen() }
        } else {
            requestAudioPermission()
        }
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


    @Composable
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

