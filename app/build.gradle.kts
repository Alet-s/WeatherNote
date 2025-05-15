plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //Google services
    id("com.google.gms.google-services")
    //Hilt
    alias(libs.plugins.hiltAndroid)
    //Kapt
    id("kotlin-kapt")
}

android {
    namespace = "com.alexser.weathernote"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.alexser.weathernote"
        minSdk = 27
        targetSdk = 35
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Worker
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    //Kapt
    kapt(libs.hilt.compiler)
    //Hilt
    implementation(libs.hilt.android)
    //Hilt Navigation para Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    //Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))//BOM
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")//Analytics
    implementation("com.google.firebase:firebase-auth-ktx")//Servicio de autenticación
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.3") // Firestore
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}