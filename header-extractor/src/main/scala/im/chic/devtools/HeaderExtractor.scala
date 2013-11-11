package im.chic.devtools

import java.io.{FileOutputStream, File}
import com.google.common.base.Charsets

object HeaderExtractor extends App {

  case class Config(inputDir: File = null, outputDir: File = null, flatten: Boolean = false)

  val parser = new scopt.OptionParser[Config]("header-extractor") {
    head("header-extractor", "0.1-SNAPSHOT")
    arg[File]("<input>") minOccurs (1) maxOccurs (1) action {
      (x, c) => c.copy(inputDir = x)
    } text ("input dir of headers")
    arg[File]("<output>") minOccurs (1) maxOccurs (1) action {
      (x, c) => c.copy(outputDir = x)
    } text ("output dir of headers")
    opt[Unit]("flatten") action {
      (_, c) => c.copy(flatten = true)
    } text ("generate a flattened layout in <output> dir")
    help("help") text ("prints this usage text")
  }

  parser.parse(args, Config()) map {
    config => {
      val inputDir = config.inputDir.getAbsoluteFile
      val outputDir = config.outputDir.getAbsoluteFile
      val rootDir = findBladeRoot(inputDir).getAbsoluteFile
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
        val outFile = new File(outputDir.getAbsoluteFile, f.getName)
        if (f.isDirectory) {
          if (flatten) translate(f, outputDir, rootDir, flatten)
          else translate(f, outFile, rootDir, flatten)
        } else if (isHeader(f.getName)) {
          if (outFile.createNewFile()) {
            val include = f.getAbsolutePath.replace(rootDir.getAbsolutePath, "").substring(1)
            val fileOutputStream = new FileOutputStream(outFile)
            fileOutputStream.write(header(include).getBytes(Charsets.UTF_8))
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
      fileName.endsWith(".h") || fileName.endsWith(".hh") ||
        fileName.endsWith(".hpp") || fileName.endsWith(".ipp") ||
        fileName.endsWith(".tcc")
    }
  }

  def header(include: String) =
    "#include \"%s\"\n".stripMargin.format(include)

  def findBladeRoot(dir: File): File = {
    if (null == dir) null
    else {
      val bladeRootFile = new File(dir.getAbsoluteFile, "BLADE_ROOT")
      val parentFile = dir.getAbsoluteFile.getParentFile
      if (bladeRootFile.exists && bladeRootFile.isFile) {
        dir.getAbsoluteFile
      } else if (null != parentFile && parentFile.exists()) {
        findBladeRoot(parentFile.getAbsoluteFile)
      } else {
        null
      }
    }
  }

}
