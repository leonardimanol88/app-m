package com.example.proyect0.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalView(
    viewModel: CargaViewModel,
    onAgregarClick: () -> Unit,
    onCamaraClick: () -> Unit
) {
    val listaCargas by viewModel.todasLasCargas.collectAsState(initial = emptyList())
    val gastoTotal = listaCargas.sumOf { it.costo }
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val fuelPriceState by viewModel.fuelPriceState.collectAsState()

    var cargaAEliminar by remember { mutableStateOf<com.example.proyect0.data.Carga?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarPrecioGasolina()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Combustible") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = onCamaraClick,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Tomar foto de ticket")
                }
                FloatingActionButton(
                    onClick = onAgregarClick,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar nueva carga")
                }
            }
        }
    ) { paddingValues ->

        // column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Gasto Total Acumulado", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$${"%.2f".format(gastoTotal)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    when (val estado = fuelPriceState) {
                        is FuelPriceUiState.Loading -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Consultando precio de referencia...")
                            }
                        }
                        is FuelPriceUiState.Success -> {
                            Text(
                                "Precio referencia Magna (${estado.precio.moneda})",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "$${"%.2f".format(estado.precio.precioPorLitro)} / L",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                "Data: OpenVan.camp",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        is FuelPriceUiState.Error -> {
                            Text(
                                "No se pudo cargar el precio: ${estado.mensaje}",
                                color = MaterialTheme.colorScheme.error
                            )
                            TextButton(onClick = { viewModel.cargarPrecioGasolina() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Tus registros", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            // lista de cargas
            if (listaCargas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Aún no hay cargas registradas.\nUsa el botón + para agregar la primera.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(listaCargas, key = { it.id }) { carga ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text("Fecha: ${sdf.format(Date(carga.fecha))}")
                                    Text(
                                        "Kilometraje: ${carga.kilometraje} km",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("Litros: ${carga.litros} L",
                                        style = MaterialTheme.typography.bodyMedium)
                                    Text("Presupuesto: $${"%.2f".format(carga.presupuesto)}",
                                        style = MaterialTheme.typography.bodySmall)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "$${"%.2f".format(carga.costo)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    IconButton(onClick = { cargaAEliminar = carga }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar carga",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    cargaAEliminar?.let { carga ->
        AlertDialog(
            onDismissRequest = { cargaAEliminar = null },
            title = { Text("Eliminar carga") },
            text = { Text("¿Seguro que quieres eliminar esta carga? ") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.borrarCarga(carga)
                    cargaAEliminar = null
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { cargaAEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}
