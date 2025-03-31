plugins {
    alias(libs.plugins.currencybuddy.android.feature.ui)
}

android {
    namespace = "tech.ericwathome.widget.presentation"
}

dependencies {

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.converter.domain)
    // glance
    implementation(libs.glance.appwidget)
    implementation(libs.glance.material3)
}