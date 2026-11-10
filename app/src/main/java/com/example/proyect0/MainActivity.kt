package com.example.proyect0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.example.proyect0.data.AppDatabase
import com.example.proyect0.repository.CargaRepository
import com.example.proyect0.repository.FuelPriceRepository
import com.example.proyect0.repository.UsuarioRepository
import com.example.proyect0.ui.CamaraView
import com.example.proyect0.ui.CargaView
import com.example.proyect0.ui.CargaViewModel
import com.example.proyect0.ui.LoginView
import com.example.proyect0.ui.LoginViewModel
import com.example.proyect0.ui.PrincipalView
import com.example.proyect0.ui.theme.Proyect0Theme
import com.example.proyect0.ui.CamaraViewModel
import java.security.Principal
import com.example.proyect0.data.remote.RetrofitClient

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val database = AppDatabase.getDatabase(applicationContext)
        val repositoryCarga = CargaRepository(database.cargaDao())
        val repositoryUsuario = UsuarioRepository(database.usuarioDao())
        val repositoryFuelPrice = FuelPriceRepository(RetrofitClient.api)

        val factoryCarga = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CargaViewModel(repositoryCarga, repositoryFuelPrice) as T
            }
        }

        val factoryUsuario = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(repositoryUsuario) as T
            }
        }

        val cargaViewModel by viewModels<CargaViewModel> { factoryCarga }
        val loginViewModel by viewModels<LoginViewModel> { factoryUsuario }
        val camaraViewModel by viewModels<CamaraViewModel>()

        // interfaz
        setContent {
            Proyect0Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    //donde arranca la app
                    NavHost(navController = navController, startDestination = "login") {


                        composable("login") {
                            LoginView(
                                viewModel = loginViewModel,
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("home") {
                            PrincipalView(
                                viewModel = cargaViewModel,
                                onAgregarClick = {
                                    navController.navigate("carga")
                                },
                                onCamaraClick = {
                                    navController.navigate("camara")
                                }
                            )
                        }

                        composable("carga") {
                            CargaView(
                                viewModel = cargaViewModel,
                                onVolverClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("camara") {
                            CamaraView(
                                viewModel = camaraViewModel,
                                onVolverClick = { navController.popBackStack() }
                            )
                        }


                        //LoginView(viewModel = loginViewModel)
                        //CargaView(viewModel = cargaViewModel)
                        // PrincipalView()
                    }
                }
            }
        }
    }
}