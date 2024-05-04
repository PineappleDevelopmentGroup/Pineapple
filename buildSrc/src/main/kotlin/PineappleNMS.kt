import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.copyTo
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.relativeTo
import kotlin.io.path.writeText

open class PineappleNMS : DefaultTask() {

    private val illogicalRelocationVersion = mapOf(
        "1.20" to "v1_20_R1",
        "1.20.1" to "v1_20_R1",
        "1.20.2" to "v1_20_R2",
        "1.20.3" to "v1_20_R3",
        "1.20.4" to "v1_20_R3",
        "1.20.5" to "v1_20_R4",
        "1.20.6" to "v1_20_R4"
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

        val targetFolder = project.projectDir.toPath().resolve("spigot-$targetVersion")
        val sourceFolder = project.projectDir.toPath().resolve("spigot-$sourceVersion")
        logger.debug("Starting create module for target version $target")

        val replacements = mapOf(
            sourceVersion to targetVersion, source to target!!
        )

        copyFolderAndReplace(sourceFolder, targetFolder, replacements)
    }

    private fun copyFolderAndReplace(source: Path, target: Path, replacements: Map<String, String>) {
        Files.walkFileTree(source, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (dir.endsWith("build")) return FileVisitResult.SKIP_SUBTREE
                val relative = relativeToAndReplace(dir, source, replacements)
                dir.copyTo(target.resolve(relative))
                return FileVisitResult.CONTINUE
            }

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                val destFile = project.projectDir.toPath()
                    .resolve(relativeToAndReplace(file, project.projectDir.toPath(), replacements))
                if (destFile.notExists()) {
                    destFile.createFile()
                }
                replaceFileContent(file, destFile, replacements)
                return FileVisitResult.CONTINUE
            }
        })
    }

    private fun replaceFileContent(originalFile: Path, file: Path, replacements: Map<String, String>) {
        var content = originalFile.readText()
        replacements.forEach { (key, value) ->
            content = content.replace(key, value)
        }
        file.writeText(content)
    }

    private fun relativeToAndReplace(file: Path, source: Path, replacements: Map<String, String>): Path {
        val relative = file.relativeTo(source)
        var replacedName = relative.toString()
        replacements.forEach { (k, v) -> replacedName = replacedName.replace(k, v) }
        return Path.of(replacedName)
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
