package im.chic.devtools

import java.io.{OutputStreamWriter, FileOutputStream, File}
import com.google.common.base.Charsets
import scala.io.Source

object HeaderReplacer extends App {

  case class Config(header: String = "", replacer: String = "", dir: File = null)

  val parser = new scopt.OptionParser[Config]("header-replacer") {
    head("header-replacer", "0.1-SNAPSHOT")
    arg[String]("<header>") minOccurs (1) maxOccurs (1) action {
      (x, c) => c.copy(header = x)
    } text ("the header to be replaced")
    arg[String]("<replacer>") minOccurs (1) maxOccurs (1) action {
      (x, c) => c.copy(replacer = x)
    } text ("replacer")
    arg[File]("<dir>") minOccurs (1) maxOccurs (1) action {
      (x, c) => c.copy(dir = x)
    } text ("the dir in which headers will be processed")
    help("help") text ("prints this usage text")
  }

  parser.parse(args, Config()) map {
    config => {
      if (null != config.dir && config.dir.exists) translate(config.dir, config.header, config.replacer)
      else println("dir " + config.dir + " does not exist.")
    }
  }

  def translate(dir: File, header: String, replacer: String) {
    if (dir.exists)
      dir.listFiles.foreach(f => {
        if (f.isDirectory) translate(f, header, replacer)
        else processFile(f, header, replacer)
      })
    else println("dir " + dir + " does not exist.")
  }

  def processFile(file: File, header: String, replacer: String) {
    if (isCppSource(file.getName)) {
      try {
        val lines = Source.fromFile(file, Charsets.UTF_8.toString).getLines.toList
        val writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)
        lines.map(processLine(_, header, replacer)).foreach(line => writer.write(line + "\n"))
        writer.flush
        writer.close
      } catch {
        case t: Throwable => println("bad file: " + file.getAbsolutePath)
      }
    }
  }

  def processLine(line: String, header: String, replacer: String) = {
    val pattern = """ #include(\s*)[<"]%s[>"] """.format(header).trim
    line.replaceAll(pattern, """#include "%s" """.trim.format(replacer))
  }

  def isCppSource(filename: String) = {
    val sourcePostfixSet = Set(".h", ".hh", "hpp", ".c", ".cc", ".cpp", ".cxx")
    (null != filename) && sourcePostfixSet.map(filename endsWith _).reduce((_1, _2) => _1 || _2)
  }

}
