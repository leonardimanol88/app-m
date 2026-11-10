# Fuelly — Historial de Cargas de Combustible

Aplicación Android nativa en Kotlin + Jetpack Compose para registrar y consultar
las cargas de gasolina de un vehículo, con cálculo de gasto total, persistencia local,
precio de referencia del mercado y captura de tickets(imagenes).

> Se entrega la base funcional del proyecto (arquitectura,
> persistencia, API externa y cámara). 

---

## Autor

- Nombre: Leonardo Imanol Linares Alvarez


---

## Descripción

La app permite al usuario:

- Registrarse e iniciar sesión (persistencia local con Room).
- Registrar cargas de combustible con kilometraje, litros, presupuesto y costo.
- Consultar el historial completo con fecha y hora.
- Ver el gasto total acumulado y el precio promedio por litro.
- Consultar el precio de referencia de la gasolina Magna en México consumiendo
  una API externa (OpenVan.camp).
- Tomar foto del ticket desde la cámara del dispositivo (guardada en galería).
- Eliminar cargas del historial con confirmación.

---

## Stack 

| Capa | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose (Material 3) |
| Patrón de arquitectura | MVVM |
| Navegación | Navigation Compose |
| Persistencia local | Room (SQLite) |
| Red | Retrofit 2 + Kotlinx Serialization |
| API externa | [OpenVan.camp Fuel Prices](https://openvan.camp/) (licencia CC BY 4.0) |
| Concurrencia | Kotlin Coroutines + StateFlow |
| Cámara | CameraX + FileProvider |
| Build | Gradle Kotlin DSL |

---

## Arquitectura (MVVM)

El proyecto sigue el patrón MVVM con separación de capas:

```
ui/          → Pantallas (@Composable) + ViewModels (estado y lógica de UI)
repository/  → Fuente de datos: Room (local) y Retrofit (remoto)
data/        → Entidades, DAOs y AppDatabase
data/remote/ → DTOs, interfaz Retrofit y cliente HTTP
```

### Flujo de datos

```
UI (@Composable)  ──observa──►  StateFlow del ViewModel
      │                              ▲
      │ callbacks                    │ emite nuevo estado
      ▼                              │
  ViewModel  ──llama──►  Repository  ──►  Room / API
                                          (fuente de verdad)
```

Principios aplicados

- La View nunca accede a la base de datos ni a Retrofit directamente.
- El ViewModel nunca conoce a Compose: expone StateFlow y sealed interface
  para los estados de carga, éxito y error.
- El Repository encapsula la fuente de datos y devuelve Result<T> para
  manejar fallos sin lanzar excepciones a la UI.
- Room emite Flow<List<Carga>> → la lista del historial se actualiza
  automáticamente al insertar o eliminar, sin código de refresco manual.

---

## API externa

- Endpoint: https://openvan.camp/api/fuel/prices
- Uso: precio de referencia de la gasolina Magna en México (MXN/litro).
- Manejo de estado: Loading / Success / Error con botón de reintentar.
- Atribución: los datos se muestran con "Data: OpenVan.camp" en pantalla,
  conforme a la licencia CC BY 4.0.
- Validación: si la API no devuelve dato válido para México, la UI muestra
  un mensaje de error (no un `$0.00`).

Ejemplo de respuesta (extracto)
"MX": {
  "country_name": "Mexico",
  "currency": "MXN",
  "unit": "liter",
  "prices": {
    "gasoline": 23.8206,
    "gasoline_premium": 28.6686,
    "diesel": 26.9833
  }
}


---

##  Cómo compilar y ejecutar

### Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Android SDK 34
- Dispositivo o emulador con API 26+ (Android 8.0+)

### Pasos

1. Clona el repositorio:
   
   git clone https://github.com/[tu-usuario]/[tu-repo].git
   
2. Abre el proyecto en Android Studio.
3. Espera el Gradle Sync (descarga de dependencias).
4. Conecta un dispositivo o inicia un emulador.
5. Presiona Run ▶ (Shift + F10).

## Estructura del proyecto

```
app/src/main/
├── java/com/example/proyect0/
│   ├── MainActivity.kt                 
│   ├── data/
│   │   ├── AppDatabase.kt              
│   │   ├── Carga.kt / Usuario.kt       
│   │   ├── CargaDao.kt / UsuarioDao.kt 
│   │   └── remote/                     Retrofit + DTOs
│   │       ├── PrecioGas.kt
│   │       ├── PrecioGasDto.kt
│   │       └── RetrofitClient.kt
│   ├── repository/
│   │   ├── CargaRepository.kt
│   │   ├── UsuarioRepository.kt
│   │   └── FuelPriceRepository.kt
│   └── ui/
│       ├── LoginView.kt / LoginViewModel.kt
│       ├── PrincipalView.kt
│       ├── CargaView.kt / CargaViewModel.kt
│       ├── CamaraView.kt / CamaraViewModel.kt
│       └── theme/                      
└── res/                                
```

---

## Casos de prueba realizados

- Registro e inicio de sesión persisten tras cerrar la app.
- Insertar una carga desde el formulario se refleja en el historial al volver.
- La lista se actualiza automáticamente al eliminar una carga.
- El precio de referencia se consulta al abrir Home (con spinner y reintento).
- La foto del ticket se guarda en la galería del dispositivo.
- Los datos se conservan al reiniciar la app (Room).
- La navegación entre pantallas funciona con el botón atrás del sistema.
- Al rotar la pantalla, el estado del ViewModel se conserva.

---
- Este proyecto es de uso académico.