tasks.register("renameApp") {
    group = "Template"
    description =
        "Rename the app name and package name. Usage: ./gradlew renameApp -PnewName=\"MyNewApp\" -PnewPackage=\"com.example.newapp\""

    doLast {
        val newName = project.findProperty("newName") as? String ?: run {
            println("Error: Please provide -PnewName=\"YourAppName\"")
            return@doLast
        }
        val newPackage = project.findProperty("newPackage") as? String ?: run {
            println("Error: Please provide -PnewPackage=\"com.your.package\"")
            return@doLast
        }

        val oldPackage = "com.smach.scriptside"
        val oldName = "backend"
        val oldPackagePath = oldPackage.replace(".", "/")
        val newPackagePath = newPackage.replace(".", "/")

        println("Step 1: Renaming text occurrences from $oldPackage to $newPackage...")

        // 1. Rename occurrences in files
        fileTree(projectDir) {
            exclude(
                ".gradle/**",
                ".idea/**",
                "**/build/**",
                "build-logic/build/**",
                "local.properties",
                "**/*.png",
                "**/*.jar",
                "**/*.bin"
            )
        }.forEach { file ->
            if (file.isFile) {
                var content = file.readText()
                var changed = false

                if (content.contains(oldPackage)) {
                    content = content.replace(oldPackage, newPackage)
                    changed = true
                }

                // Targeting rootProject.name in settings.gradle.kts
                if (file.name == "settings.gradle.kts" && content.contains("rootProject.name = \"$oldName\"")) {
                    content = content.replace(
                        "rootProject.name = \"$oldName\"",
                        "rootProject.name = \"$newName\""
                    )
                    changed = true
                }

                if (changed) {
                    file.writeText(content)
                }
            }
        }

        println("Step 2: Moving directories from $oldPackagePath to $newPackagePath...")

        // 2. Rename directories in all modules
        val kotlinDirs = mutableListOf<File>()
        projectDir.walkTopDown().forEach { file ->
            if (file.isDirectory && (file.path.endsWith("src/main/kotlin/$oldPackagePath") ||
                        file.path.endsWith("src/test/kotlin/$oldPackagePath"))
            ) {
                kotlinDirs.add(file)
            }
        }

        kotlinDirs.forEach { oldDir ->
            val basePath = oldDir.path.substring(0, oldDir.path.lastIndexOf(oldPackagePath))
            val newDir = File(basePath + newPackagePath)

            println("Moving ${oldDir.path} -> ${newDir.path}")

            if (!newDir.exists()) {
                newDir.mkdirs()
            }

            // Move all sub-packages (app, common, database, etc.)
            oldDir.listFiles()?.forEach { subFile ->
                subFile.renameTo(File(newDir, subFile.name))
            }

            // Cleanup old empty folders
            var currentOld = oldDir
            repeat(oldPackage.split(".").size) {
                if (currentOld.exists() && (currentOld.listFiles()?.isEmpty() == true)) {
                    val parent = currentOld.parentFile
                    currentOld.delete()
                    currentOld = parent
                }
            }
        }

        println("Renaming complete! Please sync Gradle and rebuild the project.")
    }
}

tasks.register<Copy>("installGitHooks") {
    description = "Installs the pre-commit git hooks."
    group = "Template"
    from("$rootDir/git-hooks/") {
        include("**/*.sh", "**/*pre-commit*")
    }
    into("$rootDir/.git/hooks")

    // Setting executable permissions
    //fileMode = 493 // 0755 in decimal
}
