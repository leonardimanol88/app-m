package com.example.proyect0.ui


import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

import android.media.MediaScannerConnection
import android.os.Environment
import java.io.File

// estados posibles de la app
sealed class CamaraUiState {
    object Initial : CamaraUiState()
    object CameraReady : CamaraUiState()
    data class PhotoCaptured(val uri: Uri) : CamaraUiState()
    data class Error(val message: String) : CamaraUiState()
}

class CamaraViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<CamaraUiState>(CamaraUiState.Initial)
    val uiState: StateFlow<CamaraUiState> = _uiState.asStateFlow()

    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraExecutor: Executor
    private var preview: Preview? = null
    private var cameraSelector: CameraSelector? = null

    fun initializeCamera(context: Context, lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        try {
            cameraExecutor = ContextCompat.getMainExecutor(context)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                try {
                    cameraProvider = cameraProviderFuture.get()

                    preview = Preview.Builder()
                        .setTargetRotation(previewView.display.rotation)
                        .build()
                        .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(previewView.display.rotation)
                        .build()

                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider?.unbindAll()
                    cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector!!, preview, imageCapture)

                    _uiState.value = CamaraUiState.CameraReady
                } catch (e: Exception) {
                    _uiState.value = CamaraUiState.Error("Error al inicializar la cámara: ${e.message}")
                }
            }, cameraExecutor)
        } catch (e: Exception) {
            _uiState.value = CamaraUiState.Error("Error al preparar la cámara: ${e.message}")
        }
    }

    fun takePhoto(context: Context) {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(System.currentTimeMillis())

        // para android10
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "Ticket_$name")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Fuelly")
            }

            val outputOptions = ImageCapture.OutputFileOptions.Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

            imageCapture.takePicture(
                outputOptions, cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = output.savedUri ?: return
                        viewModelScope.launch { _uiState.value = CamaraUiState.PhotoCaptured(savedUri) }
                    }
                    override fun onError(exc: ImageCaptureException) {
                        viewModelScope.launch { _uiState.value = CamaraUiState.Error("Error: ${exc.message}") }
                    }
                }
            )
        } else {

            val publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val fuellyDirectory = File(publicDirectory, "Fuelly")
            if (!fuellyDirectory.exists()) {
                fuellyDirectory.mkdirs() // Crea la carpeta si no existe
            }
            val photoFile = File(fuellyDirectory, "Ticket_$name.jpg")

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture.takePicture(
                outputOptions, cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                        MediaScannerConnection.scanFile(context, arrayOf(photoFile.absolutePath), arrayOf("image/jpeg")) { _, uri ->
                            viewModelScope.launch {
                                _uiState.value = CamaraUiState.PhotoCaptured(uri ?: Uri.fromFile(photoFile))
                            }
                        }
                    }
                    override fun onError(exc: ImageCaptureException) {
                        viewModelScope.launch { _uiState.value = CamaraUiState.Error("Error: ${exc.message}") }
                    }
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = CamaraUiState.Initial
    }

    override fun onCleared() {
        super.onCleared()
        cameraProvider?.unbindAll()
    }
}