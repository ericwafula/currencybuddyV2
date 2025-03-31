
plugins {
    alias(libs.plugins.currencybuddy.android.application.compose)
    alias(libs.plugins.currencybuddy.jvm.ktor)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
}

android {
    namespace = "tech.ericwathome.currencybuddy"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.koin.compose)
    implementation(libs.bundles.compose)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    implementation(libs.bundles.koin)

    api(libs.play.feature.delivery)
    api(libs.play.feature.delivery.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Location
    implementation(libs.google.android.gms.play.services.location)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Timber
    implementation(libs.timber)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.datasource.remote)
    implementation(projects.core.datasource.local)
    implementation(projects.core.notification)
    implementation(projects.converter.data)
    implementation(projects.converter.domain)
    implementation(projects.converter.presentation)
    implementation(projects.auth.data)
    implementation(projects.auth.domain)
    implementation(projects.auth.presentation)
    implementation(projects.widget.presentation)
}
