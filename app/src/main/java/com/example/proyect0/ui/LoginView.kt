package com.example.proyect0.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.proyect0.R


@Composable
fun LoginView(viewModel: LoginViewModel,
              onLoginSuccess: () -> Unit) {

    var usuario by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }

    val mensaje by viewModel.mensajeLogin.collectAsState()

    LaunchedEffect(mensaje) {
        if (mensaje.contains("Bienvenido")) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.oil_station_7787257),
            contentDescription = "Logo de Fuelly",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        Text(

            text = "Bienvenido a Fuellyyyyy",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))


        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") }
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation() // Oculta el texto
        )

        Spacer(modifier = Modifier.height(32.dp))

        // botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                viewModel.verificarCredenciales(usuario, contrasena)
            }) {
                Text("Iniciar Sesión")
            }
            OutlinedButton(onClick = {
                viewModel.registrarUsuario(usuario, contrasena)
            }) {
                Text("Registrarse")
            }
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginViewPreview() {

    //LoginView()
}