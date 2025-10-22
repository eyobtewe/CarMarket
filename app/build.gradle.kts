plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.cars.cars_marketplace"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cars.cars_marketplace"
        minSdk = 30
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
//        debug {
//            // expose a build-time switch to choose fake vs real backend in code via BuildConfig.USE_FAKE
//            // debug builds default to using the FakeApiService
//            buildConfigField("boolean", "USE_FAKE", "true")
//        }
//        // ensure release explicitly disables the fake provider
//        getByName("release") {
//            buildConfigField("boolean", "USE_FAKE", "false")
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
//    kotlin {
//        jvmToolchain(11)
//    }

//    tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
//        compilerOptions { /*...*/ }
//    }
    // Use the new Kotlin compilerOptions DSL (replaces deprecated kotlinOptions.jvmTarget)
    kotlin {
        compilerOptions {
            // target JVM 11
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
//        buildConfig = true
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
    implementation(libs.androidx.compose.material.icons.extended)
    // Super cool Lottie animations
    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
//    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
//    kapt("androidx.room:room-compiler:2.6.1")
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
//    kapt("androidx.hilt:hilt-compiler:1.1.0")
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.androidx.datastore.preferences)
    // Twitter-style fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")
    testImplementation(libs.junit)
    // MockWebServer for unit/integration tests
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}