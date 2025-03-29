plugins {
    alias(libs.plugins.currencybuddy.android.library)
    alias(libs.plugins.currencybuddy.jvm.ktor)
}

android {
    namespace = "tech.ericwathome.core.network"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.timber)
    implementation(libs.google.android.gms.play.services.location)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}