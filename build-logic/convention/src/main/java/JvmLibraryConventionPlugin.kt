import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.ericwathome.convention.configureKotlinJvm

class JvmLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            configureKotlinJvm()
        }
    }
}