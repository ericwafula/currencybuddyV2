plugins {
    alias(libs.plugins.currencybuddy.android.library)
}

android {
    namespace = "tech.ericwathome.converter.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.android.gms.play.services.location)
    implementation(libs.androidx.work)
    implementation(libs.koin.android.workmanager)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.domain)
    implementation(projects.core.datasource.local)
    implementation(projects.core.data)
    implementation(projects.converter.domain)
}