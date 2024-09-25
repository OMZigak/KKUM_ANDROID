import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.google.services)
}

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

fun String.removeQuotes(): String {
    return this.replace("\"", "")
}

android {
    namespace = "com.teamkkumul.kkumul"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.teamkkumul.kkumul"
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val kakaoAppKey = properties["kakao.app.key"].toString().removeQuotes()

        buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
        manifestPlaceholders["kakaoAppkey"] = kakaoAppKey
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":feature"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.kakao.login)
    implementation(libs.timber)

    implementation(libs.dagger.hilt)
    implementation(libs.androidx.appcompat)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
