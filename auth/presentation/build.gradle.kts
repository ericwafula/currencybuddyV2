plugins {
    alias(libs.plugins.currencybuddy.android.feature.ui)
    alias(libs.plugins.mapsplatform.secrets.plugin)
}

android {
    namespace = "tech.ericwathome.auth.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.converter.domain)
    implementation(projects.core.notification)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.auth.domain)
}