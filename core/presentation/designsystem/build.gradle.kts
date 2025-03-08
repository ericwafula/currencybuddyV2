plugins {
    alias(libs.plugins.currencybuddy.android.library.compose)
}

android {
    namespace = "tech.ericwathome.core.presentation.designsystem"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
    api(libs.lottie.compose)
}