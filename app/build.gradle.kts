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
        dataBinding = true
    }

}

dependencies {
    implementation(libs.socket.io.client)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview) // ou une version plus récente
    implementation(libs.androidx.lifecycle.viewmodel.android)

    //splash
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation(libs.play.services.cast.framework)


    implementation(libs.places)
    val roomVersion = "2.6.1"
    // Retrofit pour les requêtes HTTP
    implementation(libs.retrofit)

    // Convertisseur Gson pour parser les données JSON
    implementation(libs.converter.gson)
// Coroutines
    implementation(libs.kotlinx.coroutines.android.v160)

    implementation("com.google.android.material:material:1.11.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation(libs.glide.v4120)
    annotationProcessor(libs.compiler)

// ViewModel et LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v250)
    implementation(libs.androidx.lifecycle.livedata.ktx.v250)

    implementation(libs.androidx.fragment.ktx.v162)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.swiperefreshlayout.swiperefreshlayout)
    implementation(libs.glide.v4151)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}