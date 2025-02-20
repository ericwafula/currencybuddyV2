plugins {
    alias(libs.plugins.currencybuddy.android.library)
}

android {
    namespace = "tech.ericwathome.core.notification"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.koin)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)
}