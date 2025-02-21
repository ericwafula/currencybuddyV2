plugins {
    alias(libs.plugins.currencybuddy.android.feature.ui)
    alias(libs.plugins.mapsplatform.secrets.plugin)
}

android {
    namespace = "tech.ericwathome.converter.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.converter.domain)
    implementation(projects.core.notification)
}