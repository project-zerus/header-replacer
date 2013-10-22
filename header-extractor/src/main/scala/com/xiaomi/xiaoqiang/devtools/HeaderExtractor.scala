package com.xiaomi.xiaoqiang.devtools

import java.io.{FileOutputStream, File}
import java.util.Date
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat

object HeaderExtractor extends App {

  case class Config(inputDir: String = "", outputDir: String = "", flatten: Boolean = false)

  val parser = new scopt.immutable.OptionParser[Config]("header-extractor", "0.1-SNAPSHOT") {
    def options = Seq(
      arg("input", "Input directory of headers.") {
        (v: String, c: Config) => c.copy(inputDir = v)
      },
      arg("output", "Output directory of headers.") {
        (v: String, c: Config) => c.copy(outputDir = v)
      },
      booleanOpt("f", "flatten", "Flatten the output directory") {
        (v: Boolean, c: Config) => c.copy(flatten = v)
      }
    )
  }

  parser.parse(args, Config()) map {
    config => {
      val inputDir = new File(config.inputDir).getAbsoluteFile
      val outputDir = new File(config.outputDir).getAbsoluteFile
      val rootDir = findBladeRoot(inputDir);
      if (!inputDir.exists) {
        println("Input " + inputDir + " does not exist.")
      } else if (!inputDir.isDirectory) {
        println("Input " + inputDir + " is not a directory.")
      } else if (outputDir.exists) {
        println("Output " + outputDir + " already exists.")
      } else if (null == rootDir) {
        println("Can't find BLADE_ROOT.")
      } else {
        translate(inputDir, outputDir, rootDir, config.flatten)
      }
    }
  }

  def translate(inputDir: File, outputDir: File, rootDir: File, flatten: Boolean) {
    if (outputDir.exists || outputDir.mkdirs()) {
      inputDir.listFiles.foreach(f => {
        val outFile = new File(outputDir, f.getName)
        if (f.isDirectory) {
          if (flatten) translate(f, outputDir, rootDir, flatten)
          else translate(f, outFile, rootDir, flatten)
        } else if (isHeader(f.getName)) {
          if (outFile.createNewFile()) {
            val include = f.getAbsolutePath.replace(rootDir.getAbsolutePath, "").substring(1)
            val fileOutputStream = new FileOutputStream(outFile)
            fileOutputStream.write(header(include).getBytes(StandardCharsets.UTF_8))
            fileOutputStream.flush
            fileOutputStream.close
          } else {
            println("Can't create file " + outFile + ".")
          }
        }
      })
    } else {
      println("Can't create output " + outputDir + ".")
    }

  }

  def isHeader(fileName: String) = {
    (null != fileName) && {
      fileName.endsWith(".h") || fileName.endsWith(".hh") || fileName.endsWith(".hpp")
    }
  }

  def header(include: String) =
    """
      |#include "%s"""".stripMargin.format(include) + "\n"


  def findBladeRoot(dir: File): File = {
    val bladeRootFile = new File(dir.getAbsoluteFile, "BLADE_ROOT")
    val parentFile = dir.getParentFile.getAbsoluteFile
    if (bladeRootFile.exists && bladeRootFile.isFile) {
      dir.getAbsoluteFile
    } else if (null != parentFile && parentFile.exists()) {
      findBladeRoot(parentFile)
    } else {
      null
    }
  }

}
