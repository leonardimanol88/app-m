package com.example.proyect0.ui

import android.Manifest
import android.os.Build
import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CamaraView(
    viewModel: CamaraViewModel,
    onVolverClick: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()


    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    val permissionState = rememberMultiplePermissionsState(permissionsToRequest)
    var previewView by remember { mutableStateOf<PreviewView?>(null) }


    LaunchedEffect(permissionState.allPermissionsGranted, previewView) {
        if (permissionState.allPermissionsGranted && previewView != null) {
            viewModel.initializeCamera(context, lifecycleOwner, previewView!!)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when (uiState) {
            is CamaraUiState.Initial -> {
                if (!permissionState.allPermissionsGranted) {

                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Fuelly necesita acceder a tu cámara para respaldar los tickets.")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                            Text("Otorgar Permisos")
                        }
                    }
                } else {

                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            PreviewView(ctx).apply {
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                                scaleType = PreviewView.ScaleType.FILL_CENTER
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                previewView = this
                            }
                        }
                    )
                }
            }
            is CamaraUiState.CameraReady -> {

                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { previewView ?: PreviewView(it) }
                    )

                    Button(
                        onClick = { viewModel.takePhoto(context) },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                    ) {
                        Text("Capturar Ticket")
                    }
                }
            }
            is CamaraUiState.PhotoCaptured -> {
                // Foto tomada
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("¡Ticket respaldado en la Galería!", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = { viewModel.resetState() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Tomar otra foto")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Volver al inicio")
                    }
                }
            }
            is CamaraUiState.Error -> {

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = (uiState as CamaraUiState.Error).message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.resetState() }) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}