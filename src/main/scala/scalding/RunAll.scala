
import com.twitter.scalding

import java.io.{IOException, FileInputStream, FileOutputStream, File}
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.util.jar.JarOutputStream
import java.util.zip.ZipOutputStream
import org.apache.commons.io.IOUtils
import org.apache.hadoop.util.ToolRunner
import org.apache.hadoop.conf.Configuration
import org.apache.tools.zip.ZipEntry
import pl.project13.hadoop.NoJarTool
import scala.collection.JavaConverters
import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
 * This main is intended for use only for the Activator run command as 
 * the default. If you pass no arguments, it runs all of the examples
 * using default arguments. Use the sbt command "scalding" to run any
 * of the examples with the ability to specify your own arguments.
 * See also the companion objects' main methods.
 */

object RunAll {
  def main(args: Array[String]) {
    if (args.length == 0) {
      NGrams.main(args)
      WordCount.main(args)
      FilterUniqueCountLimit.main(args)
      TfIdf.main(args)
    } else {
      Run.run(args(0), "", args)
    }
  }
}

object Run {
  def run(name: String, message: String, args: Array[String]): Int = {
    run(name, new File("/home/kmalawski/scalding-runfromsbt-not-local" + "/target/scala-2.10/classes/"), message, args)
  }

  def run(name: String, classesDir: File, message: String, args: Array[String]): Int = {
    println(s"\n==== $name " + ("===" * 20))
    println(message)
    val argsWithName = name +: args
    println(s"Running: ${argsWithName.mkString(" ")}")


    val masterIp = "10.240.57.179" // on cluster, internal ip

    val conf = new Configuration

    // make suse these are set, otherwise Cascading will use "LocalJobRunner"
    conf.setStrings("fs.default.name", s"hdfs://$masterIp:9000")
    conf.setStrings("mapred.job.tracker", s"$masterIp:9001")

    val tool = new NoJarTool(
      wrappedTool = new scalding.Tool,
      collectClassesFrom = Some(new File("target/scala-2.10/classes")),
      libJars = List(new File("/home/kmalawski/all-deps.jar"))
    )

    ToolRunner.run(conf, tool, argsWithName)
  }

  def printSomeOutput(outputFileName: String, message: String = "") = {
    if (message.length > 0) println(message)
    println("Output in $outputFileName:")
    Source.fromFile(outputFileName).getLines.take(10) foreach println
    println("...\n")
  }

}
