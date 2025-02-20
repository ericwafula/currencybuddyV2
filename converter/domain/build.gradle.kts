plugins {
    alias(libs.plugins.currencybuddy.jvm.library)
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
}
