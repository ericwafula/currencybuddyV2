plugins {
    alias(libs.plugins.currencybuddy.android.library)
    alias(libs.plugins.currencybuddy.jvm.ktor)
}

android {
    namespace = "tech.ericwathome.core.network"
}

dependencies {

    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
}