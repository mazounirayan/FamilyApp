plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.familyapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.familyapp"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding= true
    }
}

dependencies {
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.recyclerview)
    val roomVersion = "2.6.1"
    // Retrofit pour les requêtes HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Convertisseur Gson pour parser les données JSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
// Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

// ViewModel et LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0") // Version actuelle (assure-toi d'avoir la bonne version)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0") // Si tu utilises LiveData avec coroutines

    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common)
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}