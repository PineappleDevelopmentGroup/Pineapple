import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.Path

open class PineappleNMS : DefaultTask() {

    private val illogicalRelocationVersion = mapOf(
        "1.20.2" to "v1_20_R2",
        "1.20.3" to "v1_20_R3",
        "1.20.4" to "v1_20_R3"
    )

    @get:Input
    @set:Option(option = "source", description = "sets the version to copy the NMS source from")
    var source: String = "1.20.4"

    @get:Input
    @set:Option(option = "target", description = "the version to copy the NMS implementation to")
    var target: String? = null

    @TaskAction
    fun upgradeNMS() {
        verifyOptions()

        logger.debug("mapping logical version to illogical relocation version")
        val sourceVersion = illogicalRelocationVersion[source]
            ?: error("The given source version, $source, did not map to a known relocation version")
        val targetVersion = illogicalRelocationVersion[target]
            ?: error("The given target version, $target, did not map to a known relocation version")

        val targetFolder = File(project.projectDir, "spigot-$targetVersion")
        File(project.projectDir, "spigot-$sourceVersion").copyRecursively(targetFolder)

        logger.debug("Starting create module for target version $target")

        val replacements = mapOf(
            sourceVersion to targetVersion,
            source to target
        )

        logger.debug("Staring replacements")
        replaceFilesAndFileNames(targetFolder, replacements)
        logger.debug("Finished replacements")
    }

    private fun replaceFilesAndFileNames(root: File, replacements: Map<String, String>) {
        if (!root.exists() || !root.isDirectory) {
            error("The provided path does not exist or is not a directory.")
        }

        Files.walkFileTree(root.toPath(), object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                replaceFileContent(file.toFile(), replacements)
                return super.visitFile(file, attrs)
            }
        })

        val directoryRenames = mutableListOf<Path>()
        Files.walkFileTree(root.toPath(), object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                directoryRenames.add(dir)
                return FileVisitResult.CONTINUE
            }

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                directoryRenames.add(file)
                return FileVisitResult.CONTINUE
            }
        })

        directoryRenames.reverse()
        for (path in directoryRenames) {
            replaceFileNames(path.toFile(), replacements)
        }
    }

    private fun replaceFileContent(file: File, replacements: Map<String, String>) {
        var content = file.readText()
        replacements.forEach { (key, value) ->
            content = content.replace(key, value)
        }
        file.writeText(content)
        println(content)
    }

    private fun replaceFileNames(file: File, replacements: Map<String, String>): String {
        var newName = file.name
        replacements.forEach { (key, value) ->
            newName = newName.replace("%{$key}%", value)
        }
        if (newName != file.name) {
            val newFile = File(file.parent, newName)
            if (file.renameTo(newFile)) {
                return newFile.path
            } else {
                println("Failed to rename file: ${file.path} to $newName")
            }
        }
        return file.path
    }

    private fun verifyOptions() {
        if (target == null) {
            error("Can not copy to non determined version!")
        }
        if (source == target) {
            error("Can not copy version $source to $source because they are the same version!")
        }
    }


}
