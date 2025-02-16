import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import tech.ericwathome.convention.addUiLayerDependencies

class AndroidFeatureUiConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("currencybuddy.android.library.compose")
            }

            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }
}