plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.example.smsshare"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.smsshare"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_BASE_URL", "\"https://YOUR_BACKEND_HOST\"")
    }
    flavorDimensions += "role"
    productFlavors {
        create("user") {
            dimension = "role"
            applicationIdSuffix = ".user"
            resValue("string", "app_name", "SMS Share - User")
            buildConfigField("String", "APP_ROLE", "\"user\"")
        }
        create("admin") {
            dimension = "role"
            applicationIdSuffix = ".admin"
            resValue("string", "app_name", "SMS Share - Admin")
            buildConfigField("String", "APP_ROLE", "\"admin\"")
        }
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
