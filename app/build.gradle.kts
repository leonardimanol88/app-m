plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.2.10-2.0.2"

    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.proyect0"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.proyect0"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    val room_version = "2.7.0"
    val lifecycle_version = "2.8.4"

    implementation("androidx.compose.material:material-icons-extended")

    // para Room
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // para el ViewModel y el ciclo de vida
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    // para el Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // para camara camx
    val camerax_version = "1.3.2"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    // manejo de permisos
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")

    // para carga de imagenes
    implementation("io.coil-kt:coil-compose:2.6.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // Convertidor de kotlinx-serialization para Retrofit
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
    // json de kotlinx-serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")



    // firebase notificaciones
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-messaging")

}

ksp {
    arg("room.generateKotlin", "true")
}