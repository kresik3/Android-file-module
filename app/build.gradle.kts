import com.katemedia.android.plugin.gradleUtils.dependencies.PluginDependencies
import com.katemedia.android.plugin.gradleUtils.library.androidLibrary
import com.katemedia.android.plugin.gradleUtils.library.setupLibraryPublishing

plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

androidLibrary()

project.extensions.findByType(com.android.build.gradle.LibraryExtension::class.java)?.run {
    packaging {
        resources.excludes.add("**/attach_hotspot_windows.dll")
    }
}

configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")

    resolutionStrategy.force(PluginDependencies.TestLibs.supportedObjenesis)
}

dependencies {
    implementation(PluginDependencies.Libs.okhttp3)

    implementation(PluginDependencies.KataMediaLibs.logging) { isChanging = true }

    androidTestImplementation(PluginDependencies.TestLibs.kotlinTest)
    androidTestImplementation(PluginDependencies.TestLibs.kotlinTestCore)
    androidTestImplementation(PluginDependencies.TestLibs.testCore)
    androidTestImplementation(PluginDependencies.TestLibs.junitTestExt)
    androidTestImplementation(PluginDependencies.TestLibs.espressoCore)
}

setupLibraryPublishing()