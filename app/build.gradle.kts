plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "id.ac.untirta.siakangspada"
    compileSdk = 35

    defaultConfig {
        applicationId = "id.ac.untirta.siakangspada"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
