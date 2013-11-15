package im.chic.devtools

import java.io.{FileInputStream, OutputStream, FileOutputStream, File}
import scala.util.Random
import im.chic.utils.crypto.DigestUtils
import org.spongycastle.crypto.digests.SHA512Digest

object GenRandomFile extends App {

  // 4M
  val chunkSize = 4 * 1024 * 1024

  case class Config(fileSize: Long = chunkSize, outputFile: File = null)

  val parser = new scopt.OptionParser[Config]("gen-random-file") {
    head("gen-random-file", "0.1-SNAPSHOT")
    arg[Long]("<size>") minOccurs (1) maxOccurs (1) action {
      (x, c) => c.copy(fileSize = x)
    } text ("file size")
    arg[File]("<output>") minOccurs (1) maxOccurs (1) action {
      (x, c) => c.copy(outputFile = x)
    } text ("output file")
    help("help") text ("prints this usage text")
  }

  parser.parse(args, Config()) map {
    config => {
      val outputFile = config.outputFile
      val parentFile = outputFile.getParentFile
      if (null != parentFile) parentFile.mkdirs
      if (outputFile.exists) {
        println(outputFile.getPath + " already exists.")
      } else {
        if (outputFile.createNewFile) {
          val fileOutputStream = new FileOutputStream(outputFile)
          val buffer = new Array[Byte](chunkSize)
          generateChunkAndWrite(buffer, fileOutputStream, config.fileSize)
          fileOutputStream.close()
          println("")
          val fileInputStream = new FileInputStream(outputFile)
          println(DigestUtils.dgstHex(fileInputStream, new SHA512Digest()))
        } else {
          println("Failed to create " + outputFile.getPath)
        }
      }
    }
  }

  def generateChunkAndWrite(buffer: Array[Byte], outputStream: OutputStream, bytesLeft: Long) {
    Random.nextBytes(buffer)
    if (bytesLeft >= buffer.length) {
      outputStream.write(buffer)
      println(DigestUtils.dgstHex(buffer, new SHA512Digest))
      generateChunkAndWrite(buffer, outputStream, bytesLeft - buffer.length)
    } else {
      println(DigestUtils.dgstHex(buffer.slice(0, bytesLeft.toInt), new SHA512Digest))
      outputStream.write(buffer, 0, bytesLeft.toInt)
    }
  }

}
