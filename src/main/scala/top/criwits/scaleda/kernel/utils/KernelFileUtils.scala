package top.criwits.scaleda
package kernel.utils

import idea.utils.MainLogger
import kernel.project.config.ProjectConfig
import kernel.utils.serialise.YAMLHelper
import verilog.parser.{VerilogParser, VerilogParserBaseVisitor}
import verilog.utils.ModuleUtils

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.file.DeletingPathVisitor

import java.io._
import java.nio.file.{Files, Path}
import java.util.zip.ZipInputStream
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

/** Kernel file utilities
  */
object KernelFileUtils {

  /** Check whether a file name is legal. This is also applied to project, target & task names, for they will be used to
    * create files / directories.
    * @param s the string
    * @return
    */
  def isLegalName(s: String): Boolean = {
    if (s.isBlank) return false
    Seq("\\", "/", "*", "?", "\"", "\'", "<", ">", "|", ":").foreach(f => if (s.contains(f)) return false)
    true
  }

  /** Convert any path into an absolute path. For relative path, it is calculated base on project base.<br/>
    * ⚠️ Only work on same OS. Cannot process Window paths on Linux now.
    * @param path the original path string
    * @param projectBase project base
    * @return [[None]] iff no project base AND relative path; Otherwise an abspath will be returned
    */
  def toAbsolutePath(path: String, projectBase: Option[String] = ProjectConfig.projectBase): Option[String] = {
    val file = new File(path)
    if (!file.isAbsolute) {
      projectBase match {
        case Some(base) =>
          Some(new File(new File(base), path).getAbsolutePath.replace('\\', '/'))
        case None => None
      }
    } else {
      Some(file.getAbsolutePath.replace('\\', '/'))
    }
  }

  /** Convert any path into an project relative path.<br/>
    * ⚠️ Only work on same OS. Cannot process Window paths on Linux now.
    * @param path the original path string
    * @param projectBase project base
    * @return [[None]] iff no project base OR path can not be relativised; Otherwise a relative path will be returned
    */
  def toProjectRelativePath(path: String, projectBase: Option[String] = ProjectConfig.projectBase): Option[String] = {
    if (projectBase.isEmpty) return None
    val file = new File(path)
    if (file.isAbsolute) {
      val pathAbs  = java.nio.file.Paths.get(file.getAbsolutePath)
      val pathBase = java.nio.file.Paths.get(projectBase.get) // should work
      try {
        Some(pathBase.relativize(pathAbs).toString.replace('\\', '/'))
      } catch {
        case _: Throwable => None
      }
    } else {
      // Notice: won't check if it really exists
      Some(file.getPath.replace('\\', '/'))
    }
  }

  /** Recursively scan a directory for given type of file
    * @param suffixing Set of extensions
    * @param directory the directory
    * @return
    */
  def scanDirectory(suffixing: Set[String], directory: File, level: Int = 0, maxLevel: Int = 128): Seq[File] = {
    // when out of search level, return empty seq
    if (level > maxLevel) return Seq()
    val files = directory.listFiles()
    if (files == null) return Seq()

    // collect dirs
    files
      .filter(_.isDirectory)
      .map(d => scanDirectory(suffixing, d, level = level + 1, maxLevel = maxLevel))
      .foldLeft(Seq[File]())(_ ++ _) ++
      // collect files
      files
        .filter(!_.isDirectory)
        .map(f => {
          val fileName = f.getName
          if (suffixing.exists(suffix => fileName.endsWith(s".$suffix"))) Seq(f)
          else Seq()
        })
        .foldLeft(Seq[File]())(_ ++ _)
  }

  /** Get all source files from a source set.
    * @param sources list of paths, each item can be file or dir, empty string will be dropped
    * @param suffixing filter by file type
    * @return list of files
    */
  def getAllSourceFiles(sources: Set[String], suffixing: Set[String] = Set("v")): Seq[File] = {
    sources
      .filter(_.nonEmpty)
      .map(toAbsolutePath(_))
      .filter(_.nonEmpty)
      .map(f => new File(f.get))
      .map(f => {
        if (f.exists()) {
          if (f.isDirectory) scanDirectory(suffixing, f) else Seq(f)
        } else Seq()
      })
      .reduceOption(_ ++ _)
      .getOrElse(Seq())
  }

  /** Get all source files under the project. That is:
    *  - files under `source`
    *  - files given under `sources`
    * @param suffixing file type
    * @return seq of files
    */
  @Deprecated
  def getAllProjectSourceFiles(
      suffixing: Set[String] = Set("v"),
      projectConfig: ProjectConfig = ProjectConfig.config
  ): Seq[File] = {
    val sourceDir: File = new File(toAbsolutePath(projectConfig.source).get)
    val sources         = projectConfig.sources.toSet
    getAllSourceFiles(sources + sourceDir.getAbsolutePath, suffixing = suffixing)
  }

  /** Get all test files. That is:
    *  - files under `test`
    * @return
    */
  @Deprecated
  def getAllProjectTestFiles(projectConfig: ProjectConfig = ProjectConfig.config): Seq[File] = {
    val testDir = new File(toAbsolutePath(projectConfig.test).get)
    scanDirectory(Set("v"), testDir)
  }

  /** Get module titles inside a file.
    * @param verilogFile the file
    * @return Seq[String]: All module titles in that file
    */
  def getModuleTitle(verilogFile: File): Seq[String] = {
    val tree = ModuleUtils.parseVerilogFileAST(verilogFile)

    class ModuleIdentifierVisitor extends VerilogParserBaseVisitor[String] {
      val title = new ListBuffer[String]

      override def visitModule_identifier(ctx: VerilogParser.Module_identifierContext): String = {
        val identifier = ctx.identifier()
        if (identifier != null) {
          val simpleIdentifier = identifier.Simple_identifier()
          if (simpleIdentifier != null) {
            val moduleName = simpleIdentifier.getText
            title += simpleIdentifier.getText
            moduleName
          } else null
        } else null
      }
    }

    val visitor = new ModuleIdentifierVisitor
    visitor.visit(tree)
    visitor.title.toSeq
  }

  /** Get the verilog file containing specific module
    * @param module module title name
    * @param testbench is testbench? (will search from test sources, instead of sources)
    * @return
    */
  @Deprecated
  def getProjectModuleFile(module: String, testbench: Boolean = false): Option[File] = {
    val sources = if (testbench) getAllProjectTestFiles() else getAllProjectSourceFiles()
    getModuleFileFromSet(sources.map(_.getAbsolutePath).toSet, module)
  }

  /** Get the verilog file containing specific module from one file set
    * @param sources file set
    * @param module module name
    * @return optional file
    */
  def getModuleFileFromSet(sources: Set[String], module: String): Option[File] = {
    getAllSourceFiles(sources).foreach(f => {
      val readModule = getModuleTitle(f)
      val matched    = readModule.filter(_ == module)
      KernelLogger.debug(
        s"getModuleFile filter for $f, target module: $module, read module: $readModule, matched: $matched"
      )
      if (matched.nonEmpty) {
        return Some(f)
      } else None
    })
    KernelLogger.warn("cannot get module file!")
    None
  }

  /** Insert content after module head
    * @param original file from
    * @param output file to
    * @param moduleName target module name
    * @param insert content to insert
    * @return line where content inserts
    */
  def insertAfterModuleHead(original: File, output: File, moduleName: String, insert: String): Int = {
    KernelLogger.info(s"insertAfterModuleHead $original -> $output")
    val tree = ModuleUtils.parseVerilogFileAST(original)

    class ModuleHeadVisitor(val moduleName: String) extends VerilogParserBaseVisitor[Int] {
      var headerEndAt: Int = -1

      override def visitModule_head(ctx: VerilogParser.Module_headContext): Int = {
        val identifier = ctx.module_identifier()
        if (identifier != null) {
          val _identifier = identifier.identifier()
          if (_identifier != null) {
            val simpleIdentifier = _identifier.Simple_identifier()
            if (simpleIdentifier != null) {
              if (simpleIdentifier.getText == moduleName) {
                val line = ctx.getStop.getStopIndex
                headerEndAt = line
                return line
              }
            }
          }
        }

        -1
      }
    }

    val visitor = new ModuleHeadVisitor(moduleName)
    visitor.visit(tree)

    val source     = Source.fromFile(original)
    val sourceText = source.mkString
    val lineStart  = sourceText.slice(0, visitor.headerEndAt).count(_ == '\n')
    val newText    = new mutable.StringBuilder(sourceText).insert(visitor.headerEndAt + 1, insert).toString()
    source.close()

    val outputStream = new FileOutputStream(output)
    outputStream.write(newText.getBytes())
    outputStream.close()
    lineStart
  }

  /** Remove a directory in recursive mode
    * @param path must be a directory
    */
  def deleteDirectory(path: Path): Unit =
    if (path.toFile.exists()) Files.walkFileTree(path, DeletingPathVisitor.withLongCounters())

  /** Create parent directory for file creation
    * @param file target file
    * @return is parent dir created
    */
  def confirmFileParentPath(file: File): Boolean = {
    val parent = file.getParentFile
    if (!parent.exists()) parent.mkdirs()
    else true
  }

  def parseIpParentDirectory(path: File): Map[String, ProjectConfig] = {
    if (path.exists() && path.isDirectory) {
      val list = path.listFiles()
      if (list != null) {
        list
          .filter(_.isDirectory)
          .map(p => (p, parseIpInDirectory(p)))
          .filter(_._2.nonEmpty)
          .map(p => (p._1.getAbsolutePath, p._2.get))
          .toMap
      } else Map()
    } else Map()
  }

  /** Get [[ProjectConfig]] from IP directory
    * @param path ip path
    * @return optional [[ProjectConfig]]
    */
  def parseIpInDirectory(path: File): Option[ProjectConfig] = {
    val list = path
      .listFiles(new FilenameFilter {
        override def accept(file: File, s: String) = s == ProjectConfig.defaultConfigFile
      })
    if (list == null) return None
    list
      .map(f => {
        val source = Source.fromFile(f)
        val text   = source.mkString
        source.close()
        if (text.contains("exports")) {
          try {
            val projectConfig = YAMLHelper(text, classOf[ProjectConfig])
            if (projectConfig.exports.nonEmpty) Some(projectConfig)
            else None
          } catch {
            case e: Throwable => None
          }
        } else None
      })
      .headOption
      .flatten
  }

  /** Get absolute paths for a string based on projectBase, fallback to path itself
    * @param path path
    * @param projectBase base
    * @return
    */
  def parseAsAbsolutePath(path: String, projectBase: Option[String] = ProjectConfig.projectBase) =
    toAbsolutePath(path, projectBase = projectBase).getOrElse(path)

  def ipCacheDirectory = new File(ProjectConfig.projectBase.getOrElse(""), ".cache")

  def getAllCachedIpHash: Set[String] = {
    val list = ipCacheDirectory.listFiles()
    if (list != null)
      list.filter(_.isDirectory).map(_.getName).toSet
    else Set()
  }

  /** Remove an ip cache by hash
    * @param hash ip file hash
    */
  def removeIpFileCache(hash: String): Unit = {
    val parent = ipCacheDirectory
    if (parent.exists()) {
      val f = new File(parent, hash)
      if (f.exists()) deleteDirectory(f.toPath)
    }
  }

  /** Update ip cache, extract .xcix file to (cacheDir)/(file hash). If targetDirectory exists and not empty, do nothing
    * @param ipFile ip file, simple target ip
    * @param hash optional hash, if not provided, will calculate hash from file
    */
  def createIpFileCache(ipFile: File, hash: Option[String] = None): Unit = {
    val hashUse         = hash.getOrElse(DigestUtils.sha256Hex(new FileInputStream(ipFile)))
    val parent          = ipCacheDirectory
    val targetDirectory = new File(parent, hashUse)
    if (targetDirectory.isFile) targetDirectory.delete()
    if (targetDirectory.isDirectory && !targetDirectory.listFiles().isEmpty) return
    if (!targetDirectory.exists()) targetDirectory.mkdirs()
    // extract as zip file to targetDirectory
    val fileInputStream = new FileInputStream(ipFile)
    try {
      val zipInputStream  = new ZipInputStream(fileInputStream)
      var zipEntry        = zipInputStream.getNextEntry
      val ZIP_BUFFER_SIZE = 1024
      val buffer          = new Array[Byte](ZIP_BUFFER_SIZE)
      while (zipEntry != null) {
        val fileName = zipEntry.getName
        if (!zipEntry.isDirectory) {
          val newFile = new File(targetDirectory, fileName)
          new File(newFile.getParent).mkdirs()
          val fileOutputStream = new FileOutputStream(newFile)
          var len              = 0
          while ({
            len = zipInputStream.read(buffer)
            len > 0
          }) {
            fileOutputStream.write(buffer, 0, len)
          }
          fileOutputStream.close()
        }
        zipInputStream.closeEntry()
        zipEntry = zipInputStream.getNextEntry
      }

      zipInputStream.closeEntry()
      zipInputStream.close()
    } catch {
      case e: Throwable =>
        MainLogger.warn("Cannot extract IP File", ipFile, ": ", e)
        e.printStackTrace()
    } finally {
      fileInputStream.close()
    }
  }

  /** Operations to update ipFiles cache.<br/>
    * 1. calculate ip file hash
    * 2. remove unused cache by hash
    * 3. update cache directory
    * @param ipFiles ip files field in [[top.criwits.scaleda.kernel.project.config.ConfigNode]] (from `getIpFiles`)
    * @param projectBase optional project base
    */
  def doUpdateIpFilesCache(ipFiles: Set[String], projectBase: Option[String] = ProjectConfig.projectBase): Unit = {
    val ipRealFiles = ipFiles
      .map(parseAsAbsolutePath(_, projectBase = projectBase))
      .map(new File(_))
      .filter(f => f.exists() && f.isFile)
    // get ip files hashes using commons-codec
    val ipFilesHashes = ipRealFiles.map(f => (f, DigestUtils.sha256Hex(new FileInputStream(f))))
    // get all hashes
    val allHashes = KernelFileUtils.getAllCachedIpHash
    // remove caches not in hash list
    allHashes.filter(h => !ipFilesHashes.exists(_._2 == h)).foreach(KernelFileUtils.removeIpFileCache)
    // update ip cache
    ipFilesHashes.foreach(h => KernelFileUtils.createIpFileCache(h._1, Some(h._2)))
  }
}
