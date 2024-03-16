import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.project
import jetbrains.buildServer.configs.kotlin.projectFeatures.ProjectReportTab
import jetbrains.buildServer.configs.kotlin.projectFeatures.projectReportTab
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.version

version = "2023.11"

project {

    buildType(Build)

    features {
        projectReportTab {
            id = "PROJECT_EXT_651"
            title = "Allure Report"
            startPage = "allure-report.zip!/allure-report/index.html"
            sourceBuildRule = ProjectReportTab.SourceBuildRule.LAST_FINISHED
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
            name = "Allure"
            id = "Allure"
            type = "allureReportGeneratorRunner"
            param("target.jdk.home", "%env.JDK_17_0%")
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
