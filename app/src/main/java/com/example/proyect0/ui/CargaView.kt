package com.example.proyect0.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


import com.example.proyect0.data.Carga

@Composable
fun CargaView(viewModel: CargaViewModel,
              onVolverClick: () -> Unit)
    {
    var kilometraje by remember { mutableStateOf("") }
    var presupuesto by remember { mutableStateOf("") }
    var litros by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }

        //  tener una variable StateFlow en CargaViewModel que exponga la lista
        val historialCargas by viewModel.todasLasCargas.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registrar Carga de Gasolina",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = kilometraje,
            onValueChange = { kilometraje = it },
            label = { Text("Kilometraje actual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = presupuesto,
            onValueChange = { presupuesto = it },
            label = { Text("Presupuesto asignado $") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = litros,
            onValueChange = { litros = it },
            label = { Text("Litros cargados") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = costo,
            onValueChange = { costo = it },
            label = { Text("Costo total ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(32.dp))


        Button(onClick = {

            val kilometrajeNum = kilometraje.toIntOrNull() ?: 0
            val presupuestoNum = presupuesto.toDoubleOrNull() ?: 0.0
            val litrosNum = litros.toDoubleOrNull() ?: 0.0
            val costoNum = costo.toDoubleOrNull() ?: 0.0

            if (kilometrajeNum > 0 && costoNum > 0) {

                viewModel.agregarCarga(
                    kilometraje = kilometrajeNum,
                    presupuesto = presupuestoNum,
                    litros = litrosNum,
                    costo = costoNum
                )

                onVolverClick()

                kilometraje = ""
                presupuesto = ""
                litros = ""
                costo = ""
            }
        }) {
            Text("Guardar Carga")
        }

//        Spacer(modifier = Modifier.height(32.dp))
//        Divider()
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "Historial de Cargas",
//            style = MaterialTheme.typography.titleLarge
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))



//       LazyColumn(modifier = Modifier.fillMaxWidth()) {
//            items(historialCargas) { carga ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text(text = "Litros: ${carga.litros} L", style = MaterialTheme.typography.bodyLarge)
//                        Text(text = "Costo: $${carga.costo}", style = MaterialTheme.typography.bodyMedium)
//                    }
//                }
//            }
//        }


    }
}