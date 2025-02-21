plugins {
    alias(libs.plugins.currencybuddy.android.library.compose)
}

android {
    namespace = "tech.ericwathome.core.presentation.ui"
}

dependencies {

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
}