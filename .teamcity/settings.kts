import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.qodana
import jetbrains.buildServer.configs.kotlin.project
import jetbrains.buildServer.configs.kotlin.projectFeatures.ProjectReportTab
import jetbrains.buildServer.configs.kotlin.projectFeatures.projectReportTab
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.version

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.11"

project {

    buildType(Build)

    features {
        projectReportTab {
            title = "Allure Report"
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
        qodana {
            name = "Qoadana check"
            id = "Qoadana_check"
            enabled = false
            linter = jvm {
            }
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
