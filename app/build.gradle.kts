plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val ksPath: String? = System.getenv("KEYSTORE_PATH")

android {
    namespace = "com.fikriyustiawan.spadaweb"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fikriyustiawan.spadaweb"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        if (ksPath != null) {
            create("release") {
                storeFile = file(ksPath)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (ksPath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
}
