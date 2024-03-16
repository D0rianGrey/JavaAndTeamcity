import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2023.11"

project {

    buildType(Build)

    features {
        buildReportTab {
            id = "PROJECT_EXT_52"
            title = "Test Results"
            startPage = "allure-report/index.html"
        }
    }
}

object Build : BuildType({
    name = "Build"

    allowExternalStatus = true

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {

        maven {
            id = "Maven2"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            jdkHome = "%env.JDK_17_0_ARM64%"
        }

        step {
            name = "Copy categories.json"
            id = "CopyCategories"
            type = "simpleRunner"
            param(
                "script.content", """
                cp -f %teamcity.build.checkoutDir%/categories.json allure-results/
                """.trimIndent()
            )
            executionMode = BuildStep.ExecutionMode.ALWAYS
        }

        step {
            name = "Allure"
            id = "Allure"
            type = "allureReportGeneratorRunner"
            param("target.jdk.home", "%env.JDK_17_0%")
            param("allure.publish.mode", "PLAIN")
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
        feature {
            type = "allure.serverBuildFeature"
        }
        dockerSupport {
            enabled = false
        }

    }

})
